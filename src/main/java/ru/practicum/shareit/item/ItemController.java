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

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable int itemId) {
        return ItemMapper.toItemDto(itemService.getById(itemId));
    }


    @GetMapping
    public Collection<ItemDto> getAll(@RequestHeader(Constants.HEADER_USER_ID) int userId) {
        return ItemMapper.toItemDtos(itemService.getAll(userId));
    }

    @PostMapping
    public ItemDto create(@Validated(Create.class) @RequestBody ItemDto itemDto,
                          @RequestHeader(Constants.HEADER_USER_ID) int userId) {
        itemDto.setOwnerId(userId);
        return ItemMapper.toItemDto(itemService.create(ItemMapper.toItem(itemDto)));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable int itemId,
                          @RequestHeader(Constants.HEADER_USER_ID) int userId) {
        itemDto.setId(itemId);
        return ItemMapper.toItemDto(itemService.update(ItemMapper.toItem(itemDto), userId));
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable int itemId) {
        itemService.delete(itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam String text,
                                      @RequestHeader(Constants.HEADER_USER_ID) int userId) {
        return ItemMapper.toItemDtos(itemService.search(text, userId));
    }
}
