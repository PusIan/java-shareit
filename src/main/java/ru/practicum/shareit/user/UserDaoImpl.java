package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {
    private final Map<Integer, User> users;
    private int id = 0;

    public UserDaoImpl() {
        this.users = new HashMap<>();
    }

    @Override
    public User getById(int userId) {
        return users.get(userId);
    }

    @Override
    public User create(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User update(User user) {
        if (user.getEmail() != null) {
            users.get(user.getId()).setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            users.get(user.getId()).setName(user.getName());
        }
        return users.get(user.getId());
    }

    @Override
    public void delete(int userId) {
        users.remove(userId);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }
}
