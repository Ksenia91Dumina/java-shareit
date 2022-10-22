package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static Item toItem(ItemDto itemDto, Long ownerId) {
        return new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                ownerId,
                (itemDto.getRequestId() != null ? itemDto.getRequestId() : null));
    }

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId() != null ? item.getRequestId() : null)
                .build();
    }

    public static List<ItemDto> getAllItemsToDto(List<Item> items) {
        return items
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public static ItemInfoDto toItemInfoDto(Item item) {
        return ItemInfoDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwnerId())
                .requestId(item.getRequestId())
                .build();
    }


}