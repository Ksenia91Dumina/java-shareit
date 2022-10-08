package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto, long userId);

    ItemDto updateItem(ItemDto itemDto, long userId, long itemId);

    List<ItemDto> getAllItems();

    ItemDto getItemById(long itemId);

    List<ItemDto> getItemsByUserId(long userId);

    List<ItemDto> searchByText(String text);
}
