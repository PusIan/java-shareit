package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface UserService {
    User getById(int userId);

    User create(User user);

    User update(User user);

    void delete(int userId);

    Collection<User> getAll();
}
