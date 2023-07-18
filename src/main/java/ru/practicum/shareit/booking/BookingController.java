package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.utils.Constants;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoResponse create(@Validated @RequestBody BookingDtoRequest bookingDtoRequest,
                                     @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return bookingService.create(bookingDtoRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse approve(@PathVariable long bookingId,
                                      @RequestParam boolean approved,
                                      @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return bookingService.approve(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse getById(@PathVariable long bookingId,
                                      @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDtoResponse> getAllByUserId(@RequestParam(defaultValue = "ALL") BookingStatusFilter state,
                                                         @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return bookingService.getAllByUserId(state, userId);
    }

    @GetMapping("/owner")
    public Collection<BookingDtoResponse> getAllByOwnerId(@RequestParam(defaultValue = "ALL") BookingStatusFilter state,
                                                          @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return bookingService.getAllByOwnerId(state, userId);
    }
}
