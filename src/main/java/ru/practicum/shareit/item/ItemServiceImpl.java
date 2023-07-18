package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingInItemDtoResponse;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.PermissionViolationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemDtoWithBookingDto getById(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("item %s not found", itemId)));
        BookingInItemDtoResponse lastBooking = null;
        BookingInItemDtoResponse nextBooking = null;
        if (item.getOwner().getId() == userId) {
            lastBooking = bookingRepository.findLastBooking(itemId)
                    .stream().map(bookingMapper::toBookingInItemDtoResponse)
                    .findAny()
                    .orElse(null);
            nextBooking = bookingRepository.findNextBooking(itemId)
                    .stream().map(bookingMapper::toBookingInItemDtoResponse)
                    .findAny()
                    .orElse(null);
        }
        Collection<CommentResponseDto> commentResponseDto = commentMapper
                .toCommentsResponseDto(commentRepository.findCommentsByItem(item));
        return itemMapper.toItemDtoWithBookingDto(item, lastBooking, nextBooking, commentResponseDto);
    }

    @Override
    public Collection<ItemDtoWithBookingDto> findByUserId(long userId) {
        Collection<ItemDtoWithBookingDto> itemDtoWithBookingDtos = new ArrayList<>();
        for (Item item : itemRepository.findItemsByOwnerId(userId)) {
            BookingInItemDtoResponse lastBooking = bookingRepository.findLastBooking(item.getId())
                    .stream().map(bookingMapper::toBookingInItemDtoResponse)
                    .findAny()
                    .orElse(null);
            BookingInItemDtoResponse nextBooking = bookingRepository.findNextBooking(item.getId())
                    .stream().map(bookingMapper::toBookingInItemDtoResponse)
                    .findAny()
                    .orElse(null);
            Collection<CommentResponseDto> commentsResponseDto = commentMapper
                    .toCommentsResponseDto(commentRepository.findCommentsByItem(item));
            itemDtoWithBookingDtos.add(itemMapper.toItemDtoWithBookingDto(item, lastBooking, nextBooking, commentsResponseDto));
        }
        return itemDtoWithBookingDtos;
    }

    @Transactional
    @Override
    public ItemDto create(ItemDto itemDto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("user %s not found", userId)));
        return itemMapper.toItemDto(itemRepository.save(itemMapper.toItem(itemDto, user)));
    }

    @Transactional
    @Override
    public ItemDto update(ItemDto itemDto, long userId) {
        Item item = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new NotFoundException(String.format("item %s not found", itemDto.getId())));
        long itemOwnerId = item.getOwner().getId();
        if (itemOwnerId == userId) {
            if (itemDto.getName() != null) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }
            return itemMapper.toItemDto(itemRepository.save(item));
        } else {
            throw new PermissionViolationException("Only owner can change item");
        }
    }

    @Transactional
    @Override
    public void delete(long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public Collection<ItemDto> search(String text, long userId) {
        if (text.isEmpty()) return Collections.emptyList();
        return itemRepository.findItemsByName(text)
                .stream().map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDto addComment(CommentDto commentDto, long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("item %s not found", itemId)));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("user %s not found", userId)));
        Collection<Booking> completedBookings = bookingRepository
                .findBookingsByBookerAndItemAndEndBeforeAndStatus(user, item, LocalDateTime.now(), BookingStatus.APPROVED);
        if (!completedBookings.isEmpty()) {
            Comment comment = commentMapper.toComment(commentDto, item, user);
            commentRepository.save(comment);
            return commentMapper.toCommentResponseDto(comment);
        } else {
            throw new BadRequestException("User has no completed booking for item");
        }
    }
}
