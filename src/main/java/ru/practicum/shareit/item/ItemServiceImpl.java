package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.PermissionViolationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserDao;

import java.util.Collection;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserDao userDao;

    @Override
    public Item getById(int itemId) {
        return itemDao.getById(itemId);
    }

    @Override
    public Collection<Item> getAll(int userId) {
        return itemDao.getAll(userId);
    }

    @Override
    public Item create(Item item) {
        if (userDao.getById(item.getOwner().getId()) == null) {
            throw new NotFoundException(String.format("User %s does not exist", item.getOwner().getId()));
        }
        item.setOwner(userDao.getById(item.getOwner().getId()));
        return itemDao.create(item);
    }

    @Override
    public Item update(Item item, int userId) {
        int itemOwnerId = itemDao.getById(item.getId()).getOwner().getId();
        if (itemOwnerId == userId) {
            return itemDao.update(item);
        } else {
            throw new PermissionViolationException("Only owner can change item");
        }
    }

    @Override
    public void delete(int itemId) {
        itemDao.delete(itemId);
    }

    @Override
    public Collection<Item> search(String text, int userId) {
        return itemDao.search(text, userId);
    }
}
