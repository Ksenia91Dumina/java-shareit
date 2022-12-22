package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemByRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutput;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class RequestModelTest {
    private final User user = new User(1L, "Name", "qwer@mail.ru");
    private final User user2 = new User(2L, "Name2", "asdf@mail.ru");
    private final LocalDateTime date = LocalDateTime.of(2022, 1, 1, 1, 1);
    private final ItemRequest request = new ItemRequest(1, "Request text_1", user, date);
    private final ItemRequest request2 = new ItemRequest(2, "Request text_2", user2, date);

    @Test
    public void objectEqualsTest() {
        List<ItemByRequestDto> items = List.of(
                ItemByRequestDto.builder().id(1).build(),
                ItemByRequestDto.builder().id(2).build()
        );

        ItemRequestOutput requestOutput1 = ItemRequestMapper.toItemRequestOutput(request, items);
        ItemRequestOutput requestOutput2 = ItemRequestMapper.toItemRequestOutput(request, items);
        Assertions.assertTrue(requestOutput1.equals(requestOutput2) &&
                requestOutput2.equals(requestOutput1));
        Assertions.assertEquals(requestOutput1.hashCode(), requestOutput2.hashCode());
        Assertions.assertTrue(requestOutput1.getRequester().equals(requestOutput2.getRequester()) &&
                requestOutput2.getRequester().equals(requestOutput1.getRequester()));
        Assertions.assertEquals(requestOutput1.getRequester().hashCode(), requestOutput2.getRequester().hashCode());

        ItemRequestDto requestDto1 = ItemRequestMapper.toItemRequestDto(request);
        ItemRequestDto requestDto2 = ItemRequestMapper.toItemRequestDto(request);
        Assertions.assertTrue(requestDto1.equals(requestDto2) &&
                requestDto2.equals(requestDto1));
        Assertions.assertEquals(requestDto1.hashCode(), requestDto2.hashCode());
        Assertions.assertTrue(requestDto1.getRequester().equals(requestDto2.getRequester()) &&
                requestDto2.getRequester().equals(requestDto1.getRequester()));
        Assertions.assertEquals(requestDto1.getRequester().hashCode(), requestDto2.getRequester().hashCode());
    }

    @Test
    public void objectNotEqualsTest() {
        List<ItemByRequestDto> items = List.of(
                ItemByRequestDto.builder().id(1).build(),
                ItemByRequestDto.builder().id(2).build()
        );

        ItemRequestOutput requestOutput1 = ItemRequestMapper.toItemRequestOutput(request, items);
        ItemRequestOutput requestOutput2 = ItemRequestMapper.toItemRequestOutput(request2, items);
        Assertions.assertFalse(requestOutput1.equals(requestOutput2) &&
                requestOutput2.equals(requestOutput1));
        Assertions.assertNotEquals(requestOutput1.hashCode(), requestOutput2.hashCode());
        Assertions.assertFalse(requestOutput1.getRequester().equals(requestOutput2.getRequester()) &&
                requestOutput2.getRequester().equals(requestOutput1.getRequester()));
        Assertions.assertNotEquals(requestOutput1.getRequester().hashCode(), requestOutput2.getRequester().hashCode());

        ItemRequestDto requestDto1 = ItemRequestMapper.toItemRequestDto(request);
        ItemRequestDto requestDto2 = ItemRequestMapper.toItemRequestDto(request2);
        Assertions.assertFalse(requestDto1.equals(requestDto2) &&
                requestDto2.equals(requestDto1));
        Assertions.assertNotEquals(requestDto1.hashCode(), requestDto2.hashCode());
        Assertions.assertFalse(requestDto1.getRequester().equals(requestDto2.getRequester()) &&
                requestDto2.getRequester().equals(requestDto1.getRequester()));
        Assertions.assertNotEquals(requestDto1.getRequester().hashCode(), requestDto2.getRequester().hashCode());
    }
}
