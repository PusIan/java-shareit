package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.utils.Constants;

import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@Validated @RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemRequestClient.create(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByUserId(@RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemRequestClient.findByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(@RequestParam(required = false) @Min(0) Integer from,
                                          @RequestParam(required = false) @Min(1) Integer size,
                                          @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemRequestClient.findAll(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findRequestById(@PathVariable long requestId,
                                                  @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemRequestClient.findById(requestId, userId);
    }
}
