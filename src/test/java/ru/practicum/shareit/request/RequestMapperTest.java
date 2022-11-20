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

public class RequestMapperTest {

    private final User user = new User(1L, "Name", "qwer@mail.ru");

    LocalDateTime date = LocalDateTime.of(2022, 1, 1, 1, 1);

    @Test
    public void mapToItemRequestTest() {
        var requestDto = ItemRequestDto.builder()
                .id(1)
                .description("Request text_1")
                .build();

        ItemRequest result = ItemRequestMapper.toItemRequest(requestDto, user);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Request text_1", result.getDescription());
        Assertions.assertEquals(1, result.getRequester().getId());
    }

    @Test
    public void mapToItemRequestDtoTest() {
        var request = new ItemRequest(1, "Request text_1", user, date);

        ItemRequestDto result = ItemRequestMapper.toItemRequestDto(request);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Request text_1", result.getDescription());
        Assertions.assertEquals(1, result.getRequester().getId());
        Assertions.assertEquals(date, request.getCreated());
    }

    @Test
    public void mapToItemRequestWithAnswerDtoTest() {
        var request = new ItemRequest(1, "Request text_1", user, date);
        List<ItemByRequestDto> items = List.of(
                ItemByRequestDto.builder().id(1).build(),
                ItemByRequestDto.builder().id(2).build()
        );
        ItemRequestOutput result = ItemRequestMapper.toItemRequestWithAnswerDto(request, items);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Request text_1", result.getDescription());
        Assertions.assertEquals(1, result.getRequester().getId());
        Assertions.assertEquals(date, request.getCreated());
        Assertions.assertEquals(1, result.getItems().get(0).getId());
    }
}
