package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingStateFetchByBooker.BookingStateFetchBookerStrategyFactory;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.PermissionViolationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImpl implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingStateFetchBookerStrategyFactory bookingStateFetchBookerStrategyFactory;

    @Override
    public BookingDtoResponse create(BookingDtoRequest bookingDtoRequest, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("user %s not found", userId)));
        Item item = itemRepository.findById(bookingDtoRequest.getItemId())
                .orElseThrow(() -> new NotFoundException(String.format("item %s not found", bookingDtoRequest.getItemId())));
        if (!item.getAvailable()) {
            throw new BadRequestException(String.format("item %s not available", item.getId()));
        }
        if (item.getOwner().getId() == userId) {
            throw new NotFoundException(String.format("user can not book its own item", item.getId()));
        }
        Booking booking = bookingMapper.toBooking(bookingDtoRequest, item, user);
        return bookingMapper.toBookingDtoResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoResponse approve(long bookingId, boolean approved, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("booking %s not found", bookingId)));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new PermissionViolationException("Only item owner can approve booking");
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new BadRequestException(String.format("booking %s is already approved", bookingId));
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingMapper.toBookingDtoResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoResponse getById(long bookingId, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("user %s not found", userId)));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("booking %s not found", bookingId)));
        if (!Objects.equals(booking.getBooker(), user) && !Objects.equals(booking.getItem().getOwner(), user)) {
            throw new NotFoundException("Booking was not booked by user and item owner is different");
        }
        return bookingRepository.findById(bookingId)
                .map(bookingMapper::toBookingDtoResponse)
                .orElseThrow(() -> new NotFoundException(String.format("booking %s not found", bookingId)));
    }

    @Override
    public Collection<BookingDtoResponse> getAllByBookerId(BookingStatusFilter state, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("user %s not found", userId)));
        Collection<Booking> bookings = bookingStateFetchBookerStrategyFactory.findStrategy(state).fetch(user);
        return bookings.stream().map(bookingMapper::toBookingDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<BookingDtoResponse> getAllByItemOwnerId(BookingStatusFilter state, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("user %s not found", userId)));
        Collection<Booking> bookings;
        switch (state) {
            case CURRENT:
                bookings = bookingRepository.findBookingsByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(
                        user, LocalDateTime.now(), LocalDateTime.now());
                break;
            case REJECTED:
                bookings = bookingRepository.findBookingsByItem_OwnerAndStatusOrderByStartDesc(user, BookingStatus.REJECTED);
                break;
            case WAITING:
                bookings = bookingRepository.findBookingsByItem_OwnerAndStatusOrderByStartDesc(user, BookingStatus.WAITING);
                break;
            case PAST:
                bookings = bookingRepository.findBookingsByItem_OwnerAndStatusInAndEndBeforeOrderByStartDesc(
                        user, List.of(BookingStatus.APPROVED, BookingStatus.WAITING), LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findBookingsByItem_OwnerAndStatusInAndStartAfterOrderByStartDesc(
                        user, List.of(BookingStatus.APPROVED, BookingStatus.WAITING), LocalDateTime.now());
                break;
            default:
                bookings = bookingRepository.findBookingsByItem_OwnerOrderByStartDesc(user);
                break;
        }
        return bookings.stream().map(bookingMapper::toBookingDtoResponse)
                .collect(Collectors.toList());
    }
}
