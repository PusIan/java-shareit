package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemDaoImpl implements ItemDao {

    private final Map<Integer, Item> items;
    private int id = 0;

    public ItemDaoImpl() {
        this.items = new HashMap<>();
    }

    @Override
    public Item getById(int itemId) {
        return items.get(itemId);
    }

    @Override
    public Collection<Item> findByUserId(int userId) {
        return this.items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Item create(Item item) {
        item.setId(++id);
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Item update(Item item) {
        if (item.getName() != null) {
            items.get(item.getId()).setName(item.getName());
        }
        if (item.getDescription() != null) {
            items.get(item.getId()).setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            items.get(item.getId()).setAvailable(item.getAvailable());
        }
        return items.get(item.getId());
    }

    @Override
    public void delete(int itemId) {
        items.remove(itemId);
    }

    @Override
    public Collection<Item> search(String text, int userId) {
        if (text.isEmpty()) return Collections.emptyList();
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable()
                )
                .collect(Collectors.toList());
    }
}
