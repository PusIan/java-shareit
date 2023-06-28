package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public User getById(int userId) {
        return userDao.getById(userId);
    }

    @Override
    public User create(User user) {
        boolean emailNotUnique = userDao.getAll().stream()
                .anyMatch(u -> Objects.equals(u.getEmail(), user.getEmail()));
        if (emailNotUnique) {
            throw new RuntimeException(String.format("email %s is not unique", user.getEmail()));
        }
        return userDao.create(user);
    }

    @Override
    public User update(User user) {
        boolean emailNotUnique = userDao.getAll().stream()
                .anyMatch(u -> Objects.equals(u.getEmail(), user.getEmail())
                        && !Objects.equals(u.getId(), user.getId()));
        if (emailNotUnique) {
            throw new RuntimeException(String.format("email %s is not unique", user.getEmail()));
        }
        return userDao.update(user);
    }

    @Override
    public void delete(int userId) {
        userDao.delete(userId);
    }

    @Override
    public Collection<User> getAll() {
        return userDao.getAll();
    }
}
