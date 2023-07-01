package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@Repository
public interface ItemDao {
    Item getById(int itemId);

    Collection<Item> findByUserId(int userId);

    Item create(Item item);

    Item update(Item item);

    void delete(int itemId);

    Collection<Item> search(String text, int userId);
}
