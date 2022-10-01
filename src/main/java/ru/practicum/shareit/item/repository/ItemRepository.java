package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item createItem(Item item);

    Item updateItem(Item item, long userId);

    Item getItemById(long itemId);

    List<Item> getAllItems();

    void deleteItemById(long itemId);
}
