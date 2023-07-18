package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.utils.Constants;
import ru.practicum.shareit.utils.Create;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @GetMapping("/{itemId}")
    public ItemDtoWithBookingDto getById(@PathVariable long itemId, @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemService.getById(itemId, userId);
    }


    @GetMapping
    public Collection<ItemDtoWithBookingDto> findByUserId(@RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemService.findByUserId(userId);
    }

    @PostMapping
    public ItemDto create(@Validated(Create.class) @RequestBody ItemDto itemDto,
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
                                      @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemService.search(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@Validated @RequestBody CommentDto commentDto,
                                         @PathVariable long itemId,
                                         @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemService.addComment(commentDto, itemId, userId);
    }
}
