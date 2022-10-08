package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotAllowedException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("ItemRepository")
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> allItems = new HashMap<>();
    private long id = 0L;


    @Override
    public Item createItem(Item item) {
        item.setId(++id);
        allItems.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item item, long userId) {
        Item itemToUpdate = getItemById(item.getId());
        if (itemToUpdate.getOwnerId() != userId) {
            throw new NotAllowedException("Пользователь с id = " + userId + " не может внести изменения");
        } else {
            for (Item itemToCheck : getAllItems()) {
                if (item.getName() != null) {
                    itemToCheck.setName(item.getName());
                }
                if (item.getDescription() != null) {
                    itemToCheck.setDescription(item.getDescription());
                }
                if (item.getAvailable() != itemToCheck.getAvailable()) {
                    itemToCheck.setAvailable(item.getAvailable());
                }
            }
        }
        return getItemById(item.getId());
    }


    @Override
    public Item getItemById(long itemId) {
        return getAllItems().stream()
                .filter(item -> item.getId() == itemId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Item> getAllItems() {
        return List.copyOf(allItems.values());
    }

    @Override
    public void deleteItemById(long itemId) {
        allItems.remove(itemId);
    }

}
