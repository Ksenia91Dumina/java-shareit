package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutput;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createRequest(ItemRequestDto itemRequestDto, long userId);

    List<ItemRequestOutput> getRequestsByUserId(long userId);

    List<ItemRequestOutput> getAllRequests(long userId, int from, Integer size);

    ItemRequestOutput getRequestById(long requestId, long userId);
}