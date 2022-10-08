package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) {
        userService.getUserById(userId);
        Item item = itemRepository.createItem(ItemMapper.toItem(itemDto, userId));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long userId, long itemId) {
        getItemById(itemId);
        userService.getUserById(userId);
        Item itemToUpdate = ItemMapper.toItem(itemDto, userId);
        itemToUpdate.setId(itemId);
        return ItemMapper.toItemDto(itemRepository.updateItem(itemToUpdate, userId));
    }

    @Override
    public List<ItemDto> getAllItems() {
        return ItemMapper.getAllItemsToDto(itemRepository.getAllItems());
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
        userService.getUserById(userId);
        return itemRepository.getAllItems()
                .stream()
                .filter(item -> item.getOwnerId() == userId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchByText(String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        String searchText = text.toLowerCase();
        return itemRepository.getAllItems().stream()
                .filter(item -> item.getName().toLowerCase().contains(searchText)
                        || item.getDescription().toLowerCase().contains(searchText))
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

}