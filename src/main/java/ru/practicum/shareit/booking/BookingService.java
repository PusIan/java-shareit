package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.Collection;

public interface BookingService {
    BookingDtoResponse create(BookingDtoRequest bookingDtoRequest, long userId);

    BookingDtoResponse approve(long bookingId, boolean approved, long userId);

    BookingDtoResponse getById(long bookingId, long userId);

    Collection<BookingDtoResponse> getAllByUserId(BookingStatusFilter state, long userId);

    Collection<BookingDtoResponse> getAllByOwnerId(BookingStatusFilter state, long userId);
}
