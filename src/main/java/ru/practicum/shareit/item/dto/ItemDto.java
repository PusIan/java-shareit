package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class ItemDto {
    private Integer id;
    @NotEmpty(message = "name can not be empty", groups = {Create.class})
    private String name;
    @NotEmpty(message = "description can not be empty", groups = {Create.class})
    private String description;
    @NotNull(message = "available can not be empty", groups = {Create.class})
    private Boolean available;
    private Integer ownerId;
}