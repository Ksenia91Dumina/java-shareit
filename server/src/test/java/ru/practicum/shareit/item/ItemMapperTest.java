package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemByRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;

public class ItemMapperTest {

    @Test
    public void mapToItemTest() {
        var itemDto = ItemDto.builder().id(1).name("Name_1").description("description for item_1")
                .available(true).build();

        Item result = ItemMapper.toItem(itemDto, 1L);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Name_1", result.getName());
        Assertions.assertEquals("description for item_1", result.getDescription());
        Assertions.assertTrue(result.getAvailable());
        Assertions.assertEquals(1, result.getOwnerId());
    }

    @Test
    public void mapToItemDtoTest() {
        var item = new Item(2, "Name_2", "description for item_2",
                true, 1, null);

        ItemDto result = ItemMapper.toItemDto(item);

        Assertions.assertEquals(2, result.getId());
        Assertions.assertEquals("Name_2", result.getName());
        Assertions.assertEquals("description for item_2", result.getDescription());
        Assertions.assertTrue(result.getAvailable());
    }

    @Test
    public void mapToItemInfoDtoTest() {
        var item = new Item(1, "Name_1", "description for item_1",
                true, 1, null);

        ItemInfoDto result = ItemMapper.toItemInfoDto(item);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Name_1", result.getName());
        Assertions.assertEquals("description for item_1", result.getDescription());
        Assertions.assertTrue(result.getAvailable());
        Assertions.assertEquals(1, result.getOwnerId());
    }

    @Test
    public void mapToItemByRequestDtoTest() {
        var item = new Item(1, "Name_1", "description for item_1",
                true, 1, 1L);

        ItemByRequestDto result = ItemMapper.toItemByRequestDto(item);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Name_1", result.getName());
        Assertions.assertEquals("description for item_1", result.getDescription());
        Assertions.assertTrue(result.getAvailable());
        Assertions.assertEquals(1, result.getOwnerId());
        Assertions.assertEquals(1L, result.getRequestId());
    }
}
