package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.utils.Create;
import ru.practicum.shareit.utils.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String name;
    @NotEmpty(message = "email can not be empty", groups = {Create.class})
    @Email(message = "email must match pattern", groups = {Create.class, Update.class})
    private String email;
}
