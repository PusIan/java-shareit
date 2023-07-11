package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import java.util.Collection;

@Component
public interface UserMapper {
    UserDto toUserDto(User user);

    Collection<UserDto> toUserDtos(Collection<User> users);

    User toUser(UserDto userDto);
}
