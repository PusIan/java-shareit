package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.utils.Constants;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid BookItemRequestDto requestDto,
                                         @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.create(requestDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@PathVariable long bookingId,
                                          @RequestParam boolean approved,
                                          @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return bookingClient.approve(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@PathVariable long bookingId,
                                          @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByBookerId(@RequestHeader(Constants.HEADER_USER_ID) long userId,
                                                   @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getAllByBookerId(state, userId, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByItemOwnerId(@RequestParam(defaultValue = "ALL") BookingState state,
                                                      @RequestHeader(Constants.HEADER_USER_ID) long userId,
                                                      @RequestParam(defaultValue = Constants.PAGE_FROM_DEFAULT) @Min(0) int from,
                                                      @RequestParam(defaultValue = Constants.PAGE_SIZE_DEFAULT) @Min(1) int size) {
        return bookingClient.getAllByItemOwnerId(state, userId, from, size);
    }

}
