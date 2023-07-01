package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@Component
public interface ItemMapper {
    ItemDto toItemDto(Item item);

    Collection<ItemDto> toItemDtos(Collection<Item> items);

    Item toItem(ItemDto itemDto);
}
