package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepositoryOld {
    Item createItem(Item item);

    Item updateItem(Item item, long userId);

    Item getItemById(long itemId);

    List<Item> getAllItems();

}
