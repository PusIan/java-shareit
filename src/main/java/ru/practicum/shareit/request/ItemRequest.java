package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ItemRequest {
    private Integer id;
    private String description;
    private User requester;
    private LocalDate created;

    public ItemRequest(Integer id) {
        this.id = id;
    }
}
