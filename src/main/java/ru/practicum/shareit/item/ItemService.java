package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@Service
public interface ItemService {
    Item getById(int itemId);

    Collection<Item> findByUserId(int userId);

    Item create(Item item);

    Item update(Item item, int userId);

    void delete(int itemId);

    Collection<Item> search(String text, int userId);
}
