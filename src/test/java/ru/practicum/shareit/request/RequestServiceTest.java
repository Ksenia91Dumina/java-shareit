package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutput;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RequestServiceTest {
    @InjectMocks
    private ItemRequestServiceImpl requestService;
    @Mock
    private ItemRequestRepository repository;
    @Mock
    private UserService userService;
    @Mock
    private ItemRepository itemRepository;

    private final LocalDateTime date = LocalDateTime.of(2022, 1, 1, 1, 1);

    private final ItemRequest itemRequest = new ItemRequest(1, "Request text",
            new User(1, "Name", "qwer@mail.ru"), date);

    private ItemRequestDto getRequestDto() {
        return ItemRequestDto.builder()
                .id(2)
                .description("Request text_2")
                .build();
    }

    @BeforeEach
    void beforeEach() {
        repository = mock(ItemRequestRepository.class);
        requestService = new ItemRequestServiceImpl(userService, itemRepository);
        requestService.repository = repository;
    }

    @Test
    public void getRequestsByUserIdTest() {
        when(repository.findAllByRequester_IdOrderByCreatedDesc(anyLong()))
                .thenReturn(List.of(itemRequest));

        List<ItemRequestOutput> result = requestService.getRequestsByUserId(1);

        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals("Request text", result.get(0).getDescription());
        Assertions.assertEquals(1, result.get(0).getRequester().getId());
        Assertions.assertEquals(date, result.get(0).getCreated());
    }

    @Test
    public void getAllRequestsTest() {
        when(repository.findAllByRequester_IdNotOrderByCreatedDesc(anyLong()))
                .thenReturn(List.of(itemRequest));

        List<ItemRequestOutput> result = requestService.getAllRequests(1, 0, 1);

        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals("Request text", result.get(0).getDescription());
        Assertions.assertEquals(1, result.get(0).getRequester().getId());
        Assertions.assertEquals(date, result.get(0).getCreated());
    }

    @Test
    public void getRequestByIdTest() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(itemRequest));

        ItemRequestOutput result = requestService.getRequestById(1, 1);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Request text", result.getDescription());
        Assertions.assertEquals(1, result.getRequester().getId());
        Assertions.assertEquals(date, result.getCreated());
    }

    @Test
    public void getRequestNotExistsByIdTest() {
        when(repository.findById(anyLong()))
                .thenThrow(new NotFoundException("Запрос с id = 1 не найден"));

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> requestService.getRequestById(1, 1));

        Assertions.assertEquals("Запрос с id = 1 не найден", exception.getMessage());
    }

}
