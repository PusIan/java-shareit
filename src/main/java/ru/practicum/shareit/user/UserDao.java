package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserDao {
    User getById(int userId);

    User create(User user);

    User update(User user);

    void delete(int userId);

    Collection<User> getAll();
}
