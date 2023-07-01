package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
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
    public ItemDto getById(@PathVariable int itemId) {
        return itemMapper.toItemDto(itemService.getById(itemId));
    }


    @GetMapping
    public Collection<ItemDto> findByIserId(@RequestHeader(Constants.HEADER_USER_ID) int userId) {
        return itemMapper.toItemDtos(itemService.findByUserId(userId));
    }

    @PostMapping
    public ItemDto create(@Validated(Create.class) @RequestBody ItemDto itemDto,
                          @RequestHeader(Constants.HEADER_USER_ID) int userId) {
        itemDto.setOwnerId(userId);
        return itemMapper.toItemDto(itemService.create(itemMapper.toItem(itemDto)));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable int itemId,
                          @RequestHeader(Constants.HEADER_USER_ID) int userId) {
        itemDto.setId(itemId);
        return itemMapper.toItemDto(itemService.update(itemMapper.toItem(itemDto), userId));
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable int itemId) {
        itemService.delete(itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam String text,
                                      @RequestHeader(Constants.HEADER_USER_ID) int userId) {
        return itemMapper.toItemDtos(itemService.search(text, userId));
    }
}
