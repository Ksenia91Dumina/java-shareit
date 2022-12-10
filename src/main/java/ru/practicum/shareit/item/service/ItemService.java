package ru.practicum.shareit.item.service;

import ru.practicum.shareit.MyPageRequest;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto, long userId);

    ItemDto updateItem(ItemDto itemDto, long userId, long itemId);

    Item getItemById(long itemId);

    ItemInfoDto getItemInfoById(long itemId, long userId);

    List<ItemInfoDto> getItemsInfoByUserId(long userId, MyPageRequest pageRequest);

    List<ItemDto> searchByText(String text, MyPageRequest pageRequest);

    CommentDto addComment(CommentDto commentDto, long userId, long itemId);
}
