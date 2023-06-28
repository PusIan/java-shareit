package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.User;

import java.sql.Date;

@Data
@AllArgsConstructor
public class ItemRequest {
    private Integer id;
    private String description;
    private User requester;
    private Date created;

    public ItemRequest(Integer id) {
        this.id = id;
    }
}
