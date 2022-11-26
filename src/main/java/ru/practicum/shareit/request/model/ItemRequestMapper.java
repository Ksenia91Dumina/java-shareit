package ru.practicum.shareit.request.model;

import ru.practicum.shareit.item.dto.ItemByRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutput;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User user) {
        return new ItemRequest(
                itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                user,
                LocalDateTime.now()
        );
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requester(new ItemRequestDto.Requester(itemRequest.getRequester()))
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequestOutput toItemRequestWithAnswerDto(ItemRequest itemRequest,
                                                               List<ItemByRequestDto> items) {
        return ItemRequestOutput.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requester(new ItemRequestOutput.Requester((itemRequest.getRequester())))
                .created(itemRequest.getCreated())
                .items(items)
                .build();
    }
}