package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.additions.MyPageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ItemServiceTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository repository;
    @Mock
    private UserService userService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestService requestService;

    private final Item item = new Item(1L, "Name_1", "description for item_1",
            true, 1L, 1L);

    private ItemDto getItemDto() {
        return ItemDto.builder()
                .name("Name_2")
                .description("description for item_2")
                .available(true)
                .build();
    }

    @BeforeEach
    void beforeEach() {
        repository = mock(ItemRepository.class);
        itemService = new ItemServiceImpl(bookingRepository, commentRepository, requestService, userService);
        itemService.itemRepository = repository;
    }

    @Test
    public void createItemTest() {
        when(repository.save(any(Item.class)))
                .thenReturn(item);

        ItemDto result = itemService.createItem(ItemMapper.toItemDto(item), 1L);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Name_1", result.getName());
        Assertions.assertEquals("description for item_1", result.getDescription());
        Assertions.assertEquals(true, result.getAvailable());
        Assertions.assertEquals(1, result.getRequestId());
    }

    @Test
    public void updateItemTest() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(repository.save(any()))
                .thenReturn(item);

        var result = itemService.updateItem(getItemDto(), 1L, 1L);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Name_2", result.getName());
        Assertions.assertEquals("description for item_2", result.getDescription());
        Assertions.assertEquals(true, result.getAvailable());
        Assertions.assertEquals(1, result.getRequestId());
    }

    @Test
    public void updateNameTest() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(repository.save(any()))
                .thenReturn(new Item(1L, "NewName", "description for item_1",
                        true, 1L, null));

        var result = itemService.updateItem(ItemDto.builder().name("NewName").build(), 1L, 1L);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("NewName", result.getName());
        Assertions.assertEquals("description for item_1", result.getDescription());
        Assertions.assertEquals(true, result.getAvailable());
        Assertions.assertEquals(1, result.getRequestId());
    }

    @Test
    public void updateDescriptionTest() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(repository.save(any()))
                .thenReturn(new Item(1L, "Name_1", "New Description",
                        true, 1L, null));

        var result = itemService.updateItem(ItemDto.builder().description("New Description").build(),
                1L, 1L);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Name_1", result.getName());
        Assertions.assertEquals("New Description", result.getDescription());
        Assertions.assertEquals(true, result.getAvailable());
        Assertions.assertEquals(1, result.getRequestId());
    }

    @Test
    public void updateAvailableTest() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(repository.save(any()))
                .thenReturn(new Item(1L, "Name_1", "description for item_1",
                        false, 1L, null));

        var result = itemService.updateItem(ItemDto.builder().available(false).build(), 1L, 1L);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Name_1", result.getName());
        Assertions.assertEquals("description for item_1", result.getDescription());
        Assertions.assertEquals(false, result.getAvailable());
        Assertions.assertEquals(1, result.getRequestId());
    }

    @Test
    public void getItemInfoByIdTest() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        ItemInfoDto result = itemService.getItemInfoById(1L, 1L);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Name_1", result.getName());
        Assertions.assertEquals("description for item_1", result.getDescription());
        Assertions.assertEquals(true, result.getAvailable());
        Assertions.assertEquals(1, result.getOwnerId());
        Assertions.assertEquals(1, result.getRequestId());
    }

    @Test
    public void getItemByIdNotExistsTest() {
        when(repository.findById(anyLong()))
                .thenThrow(new NotFoundException("Item с id = 5 не найден"));

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.getItemById(5));

        Assertions.assertEquals("Item с id = 5 не найден", exception.getMessage());
    }

    @Test
    public void getItemsInfoByUserIdTest() {
        when(repository.findAllByOwnerId(anyLong(), any(MyPageRequest.class)))
                .thenReturn(List.of(item));

        List<ItemInfoDto> result = itemService.getItemsInfoByUserId(1L, MyPageRequest.ofSize(10));

        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals("Name_1", result.get(0).getName());
        Assertions.assertEquals("description for item_1", result.get(0).getDescription());
        Assertions.assertEquals(true, result.get(0).getAvailable());
        Assertions.assertEquals(1, result.get(0).getOwnerId());
        Assertions.assertEquals(1, result.get(0).getRequestId());
    }

    @Test
    public void searchByTextTest() {
        when(repository.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableEquals(anyString(), anyString(),
                anyBoolean(), any(MyPageRequest.class)))
                .thenReturn(List.of(item));

        List<ItemDto> result = itemService.searchByText("Name_1", MyPageRequest.ofSize(10));

        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals("Name_1", result.get(0).getName());
        Assertions.assertEquals("description for item_1", result.get(0).getDescription());
        Assertions.assertEquals(true, result.get(0).getAvailable());
        Assertions.assertEquals(1, result.get(0).getRequestId());
    }

    @Test
    public void addCommentTest() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserDto.builder().build());
        when(bookingRepository.findByBookerIdAndItem_IdAndEndBefore(anyLong(), anyLong(), any()))
                .thenReturn(new Booking());
        when(commentRepository.save(any()))
                .thenReturn(new Comment(1L, "Comment for item_1", new User(), 1L, LocalDateTime.now()));

        var result = itemService.addComment(CommentDto.builder().build(), 1L, 1L);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Comment for item_1", result.getText());
        Assertions.assertEquals(1, result.getItemId());
    }

}
