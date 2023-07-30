package ru.practicum.shareit.request.dto;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface ItemRequestMapper {

    ItemRequestResponseDto toItemRequestDto(ItemRequest itemRequest);

    Collection<ItemRequestResponseDto> toItemRequestDtos(Collection<ItemRequest> itemRequests);

    Page<ItemRequestResponseDto> toItemRequestDtos(Page<ItemRequest> itemRequests);

    ItemRequest toItemRequestItem(ItemRequestDto itemRequestDto, User userId);
}
