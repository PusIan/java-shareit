package ru.practicum.shareit.booking.BookingStateFetchByBooker;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatusFilter;
import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface BookingStateFetchByBookerStrategy {
    BookingStatusFilter getStrategyName();

    Collection<Booking> fetch(User user);
}
