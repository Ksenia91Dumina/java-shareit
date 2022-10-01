package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserServiceImpl userService;

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) {
        userService.getUserById(userId);
        Item item = ItemMapper.toItem(itemDto, userId);
        itemRepository.createItem(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long userId) {
        Long itemId = ItemMapper.toItem(itemDto, userId).getId();
        itemRepository.getItemById(itemId);
        userService.getUserById(userId);
        itemRepository.updateItem(itemRepository.getItemById(itemId), userId);
        return ItemMapper.toItemDto(itemRepository.getItemById(itemId));
    }

    @Override
    public List<ItemDto> getAllItems() {
        return itemRepository.getAllItems()
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(long itemId) {
        Item item = itemRepository.getItemById(itemId);
        if (item == null) {
            throw new NotFoundException("Item с id = " + itemId + " не найден");
        }
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsByUserId(long userId) {
        List<ItemDto> allUserItems = new ArrayList<>();
        userService.getUserById(userId);
        if (!itemRepository.getAllItems().isEmpty()) {
            for (Item itemToCheck : itemRepository.getAllItems()) {
                if (itemToCheck.getOwnerId() == userId) {
                    allUserItems.add(ItemMapper.toItemDto(itemToCheck));
                }
            }
        }
        return allUserItems;
    }

    @Override
    public List<ItemDto> searchByText(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        List<ItemDto> availableItems = new ArrayList<>();
        String textToSearch = text.toLowerCase();
        for (Item itemToCheck : itemRepository.getAllItems()) {
            if (itemToCheck.getName().toLowerCase().contains(textToSearch) ||
                    itemToCheck.getDescription().toLowerCase().contains(textToSearch) &&
                            itemToCheck.getAvailable()) {
                availableItems.add(ItemMapper.toItemDto(itemToCheck));
            }
        }

        return availableItems;
    }

    public void deleteItemById(long itemId) {
        getItemById(itemId);
        itemRepository.deleteItemById(itemId);
    }

}