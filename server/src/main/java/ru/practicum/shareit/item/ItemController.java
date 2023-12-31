package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingDto;
import ru.practicum.shareit.utils.Constants;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDtoWithBookingDto getById(@PathVariable long itemId, @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public Collection<ItemDtoWithBookingDto> findByUserId(@RequestHeader(Constants.HEADER_USER_ID) long userId,
                                                          @RequestParam(defaultValue = Constants.PAGE_FROM_DEFAULT) int from,
                                                          @RequestParam(defaultValue = Constants.PAGE_SIZE_DEFAULT) int size) {
        return itemService.findByUserId(userId, from, size);
    }

    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto,
                          @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable long itemId,
                          @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        itemDto.setId(itemId);
        return itemService.update(itemDto, userId);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable long itemId) {
        itemService.delete(itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam String text,
                                      @RequestHeader(Constants.HEADER_USER_ID) long userId,
                                      @RequestParam(defaultValue = Constants.PAGE_FROM_DEFAULT) int from,
                                      @RequestParam(defaultValue = Constants.PAGE_SIZE_DEFAULT) int size) {
        return itemService.search(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@RequestBody CommentDto commentDto,
                                         @PathVariable long itemId,
                                         @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemService.addComment(commentDto, itemId, userId);
    }
}
