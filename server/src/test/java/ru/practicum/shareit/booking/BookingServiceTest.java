package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.additions.MyPageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutput;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BookingServiceTest {
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Mock
    private BookingRepository repository;
    @Mock
    private ItemService itemService;
    @Mock
    private UserService userService;

    private final User user = new User(1, "Name", "qwer@mail.ru");

    private final Booking booking1 = new Booking(1,
            LocalDateTime.of(2022, 10, 1, 1, 1),
            LocalDateTime.of(2022, 11, 1, 1, 1),
            new Item(), user,
            BookingStatus.APPROVED
    );

    private final Booking booking2 = new Booking(1,
            LocalDateTime.of(2022, 12, 1, 1, 1),
            LocalDateTime.of(2022, 12, 30, 1, 1),
            new Item(), user,
            BookingStatus.WAITING
    );

    private BookingDto getBookingDto() {
        return BookingDto.builder()
                .id(3)
                .start(LocalDateTime.of(2022, 11, 1, 1, 1))
                .end(LocalDateTime.now())
                .itemId(1)
                .bookerId(1)
                .status(BookingStatus.WAITING)
                .build();
    }

    /* @BeforeEach
    void beforeEach() {
        repository = mock(BookingRepository.class);
        bookingService = new BookingServiceImpl(itemService, userService);
        bookingService.bookingRepository = repository;
    }*/

    @Test
    public void addBookingTest() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserDto.builder().build());
        when(itemService.getItemById(anyLong()))
                .thenReturn(new Item(1L, "Item", "Description for Item",
                        true, 1L, null));
        when(repository.save(any(Booking.class)))
                .thenReturn(booking1);

        var result = bookingService.addBooking(getBookingDto(), 2L);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(LocalDateTime.of(2022, 10, 1, 1, 1), result.getStart());
        Assertions.assertEquals(LocalDateTime.of(2022, 11, 1, 1, 1), result.getEnd());
        Assertions.assertEquals(1, result.getBooker().getId());
        Assertions.assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    public void addBookingWithWrongUserTest() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user));
        when(itemService.getItemById(anyLong()))
                .thenReturn(new Item(1L, "Item", "Description for Item",
                        true, 1L, null));

        final NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.addBooking(getBookingDto(), 1L));

        Assertions.assertEquals("Пользователь с id = 1 не может забронировать предмет с id = 1",
                exception.getMessage());
    }

    @Test
    public void addBookingForNotAvailableItemTest() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user));
        when(itemService.getItemById(anyLong()))
                .thenReturn(new Item(2L, "Item", "Description for Item",
                        false, 1L, null));

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.addBooking(getBookingDto(), 1L));

        Assertions.assertEquals("Пользователь с id = 1 не может забронировать предмет с id = 2",
                exception.getMessage());
    }

    @Test
    public void getBookingByIdTest() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(booking1));

        BookingOutput result = bookingService.getBookingById(1, 1);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(LocalDateTime.of(2022, 10, 1, 1, 1), result.getStart());
        Assertions.assertEquals(LocalDateTime.of(2022, 11, 1, 1, 1), result.getEnd());
        Assertions.assertEquals(1, result.getBooker().getId());
        Assertions.assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    public void getByIdNotExistsTest() {
        when(repository.findById(anyLong()))
                .thenThrow(new NotFoundException("Бронирование с id = 5 не найдено"));

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.getBookingById(1, 1));

        Assertions.assertEquals("Бронирование с id = 5 не найдено", exception.getMessage());
    }


    @Test
    public void updateApprovedStatusTest() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(booking2));
        when(itemService.getItemById(anyLong()))
                .thenReturn(new Item(1L, "Item", "Description for Item",
                        true, 1L, null));
        when(repository.save(any(Booking.class)))
                .thenReturn(new Booking(2,
                        LocalDateTime.of(2022, 12, 1, 1, 1),
                        LocalDateTime.of(2022, 12, 30, 1, 1),
                        new Item(), user,
                        BookingStatus.APPROVED));

        var result = bookingService.updateBooking(2L, true, 1L);

        Assertions.assertEquals(2, result.getId());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 1, 1, 1), result.getStart());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 30, 1, 1), result.getEnd());
        Assertions.assertEquals(1, result.getBooker().getId());
        Assertions.assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    public void updateRejectedStatusTest() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(booking2));
        when(itemService.getItemById(anyLong()))
                .thenReturn(new Item(1L, "Item", "Description for Item",
                        false, 1L, null));
        when(repository.save(any(Booking.class)))
                .thenReturn(new Booking(2,
                        LocalDateTime.of(2022, 12, 1, 1, 1),
                        LocalDateTime.of(2022, 12, 30, 1, 1),
                        new Item(), user,
                        BookingStatus.REJECTED));

        var result = bookingService.updateBooking(2L, true, 1L);

        Assertions.assertEquals(2, result.getId());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 1, 1, 1), result.getStart());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 30, 1, 1), result.getEnd());
        Assertions.assertEquals(1, result.getBooker().getId());
        Assertions.assertEquals(BookingStatus.REJECTED, result.getStatus());
    }

    @Test
    public void updateBookingWithWrongUserTest() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(booking1));
        when(itemService.getItemById(anyLong()))
                .thenReturn(new Item(1L, "Item", "description for Item",
                        true, 1L, null));

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.updateBooking(1L, true, 2L));

        Assertions.assertEquals("Пользователь с id = 2 не может внести изменение в бронирование",
                exception.getMessage());
    }

    @Test
    public void getAllBookingItemsByOwnerIdTest() {
        booking1.getItem().setOwnerId(2);
        when(repository.findAllByItem_OwnerId(anyLong(), any(Sort.class), any(MyPageRequest.class)))
                .thenReturn(List.of(booking1));

        List<BookingOutput> result = bookingService.getBookingItemsByOwnerId(BookingState.ALL,
                2, MyPageRequest.ofSize(1));

        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(LocalDateTime.of(2022, 10, 1, 1, 1),
                result.get(0).getStart());
        Assertions.assertEquals(LocalDateTime.of(2022, 11, 1, 1, 1),
                result.get(0).getEnd());
        Assertions.assertEquals(1, result.get(0).getBooker().getId());
        Assertions.assertEquals(BookingStatus.APPROVED, result.get(0).getStatus());
    }

    @Test
    public void getPastBookingItemsByOwnerIdTest() {
        booking1.getItem().setOwnerId(2);
        when(repository.findAllByItem_OwnerIdAndEndBefore(anyLong(), any(LocalDateTime.class),
                any(Sort.class), any(MyPageRequest.class)))
                .thenReturn(List.of(booking1));

        List<BookingOutput> result = bookingService.getBookingItemsByOwnerId(BookingState.PAST,
                2, MyPageRequest.ofSize(1));

        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(LocalDateTime.of(2022, 10, 1, 1, 1),
                result.get(0).getStart());
        Assertions.assertEquals(LocalDateTime.of(2022, 11, 1, 1, 1),
                result.get(0).getEnd());
        Assertions.assertEquals(1, result.get(0).getBooker().getId());
        Assertions.assertEquals(BookingStatus.APPROVED, result.get(0).getStatus());
    }

    @Test
    public void getFutureBookingItemsByOwnerIdTest() {
        Booking booking3 = new Booking(1,
                LocalDateTime.of(2022, 12, 1, 1, 1),
                LocalDateTime.of(2022, 12, 30, 1, 1),
                new Item(), user,
                BookingStatus.APPROVED
        );
        booking3.getItem().setOwnerId(2);
        when(repository.findAllByItem_OwnerIdAndStartAfter(anyLong(), any(LocalDateTime.class),
                any(Sort.class), any(MyPageRequest.class)))
                .thenReturn(List.of(booking3));

        List<BookingOutput> result = bookingService.getBookingItemsByOwnerId(BookingState.FUTURE,
                2, MyPageRequest.ofSize(1));

        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 1, 1, 1),
                result.get(0).getStart());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 30, 1, 1),
                result.get(0).getEnd());
        Assertions.assertEquals(1, result.get(0).getBooker().getId());
        Assertions.assertEquals(BookingStatus.APPROVED, result.get(0).getStatus());
    }

    @Test
    public void getWaitingBookingItemsByOwnerIdTest() {
        booking2.getItem().setOwnerId(2);
        when(repository.findAllByItem_OwnerIdAndStatusEquals(anyLong(), any(BookingStatus.class),
                any(Sort.class), any(MyPageRequest.class)))
                .thenReturn(List.of(booking2));

        List<BookingOutput> result = bookingService.getBookingItemsByOwnerId(BookingState.WAITING,
                2, MyPageRequest.ofSize(1));

        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 1, 1, 1),
                result.get(0).getStart());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 30, 1, 1),
                result.get(0).getEnd());
        Assertions.assertEquals(1, result.get(0).getBooker().getId());
        Assertions.assertEquals(BookingStatus.WAITING, result.get(0).getStatus());
    }

    @Test
    public void getRejectedBookingItemsByOwnerIdTest() {
        Booking booking3 = new Booking(1,
                LocalDateTime.of(2022, 12, 1, 1, 1),
                LocalDateTime.of(2022, 12, 10, 1, 1),
                new Item(), user,
                BookingStatus.REJECTED
        );
        booking3.getItem().setOwnerId(2);
        when(repository.findAllByItem_OwnerIdAndStatusEquals(anyLong(), any(BookingStatus.class),
                any(Sort.class), any(MyPageRequest.class)))
                .thenReturn(List.of(booking3));

        List<BookingOutput> result = bookingService.getBookingItemsByOwnerId(BookingState.REJECTED,
                2, MyPageRequest.ofSize(1));

        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 1, 1, 1),
                result.get(0).getStart());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 10, 1, 1),
                result.get(0).getEnd());
        Assertions.assertEquals(1, result.get(0).getBooker().getId());
        Assertions.assertEquals(BookingStatus.REJECTED, result.get(0).getStatus());
    }

    @Test
    public void getCurrentBookingItemsByOwnerIdTest() {
        Booking booking4 = new Booking(1,
                LocalDateTime.of(2022, 11, 1, 1, 1),
                LocalDateTime.of(2022, 12, 10, 1, 1),
                new Item(), user,
                BookingStatus.APPROVED
        );
        booking4.getItem().setOwnerId(2);
        when(repository.findAllByItem_OwnerIdAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class),
                any(LocalDateTime.class), any(Sort.class), any(MyPageRequest.class)))
                .thenReturn(List.of(booking4));

        List<BookingOutput> result = bookingService.getBookingItemsByOwnerId(BookingState.CURRENT,
                2, MyPageRequest.ofSize(1));

        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(LocalDateTime.of(2022, 11, 1, 1, 1),
                result.get(0).getStart());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 10, 1, 1),
                result.get(0).getEnd());
        Assertions.assertEquals(1, result.get(0).getBooker().getId());
        Assertions.assertEquals(BookingStatus.APPROVED, result.get(0).getStatus());
    }

    @Test
    public void getAllBookingsByUserIdTest() {
        when(repository.findAllByBookerId(anyLong(), any(Sort.class), any(MyPageRequest.class)))
                .thenReturn(List.of(booking1));

        List<BookingOutput> result = bookingService.getBookingsByUserId(BookingState.ALL,
                1, MyPageRequest.ofSize(1));

        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(LocalDateTime.of(2022, 10, 1, 1, 1),
                result.get(0).getStart());
        Assertions.assertEquals(LocalDateTime.of(2022, 11, 1, 1, 1),
                result.get(0).getEnd());
        Assertions.assertEquals(1, result.get(0).getBooker().getId());
        Assertions.assertEquals(BookingStatus.APPROVED, result.get(0).getStatus());
    }

    @Test
    public void getPastBookingsByUserIdTest() {
        when(repository.findAllByBookerIdAndEndBefore(anyLong(), any(LocalDateTime.class),
                any(Sort.class), any(MyPageRequest.class)))
                .thenReturn(List.of(booking1));

        List<BookingOutput> result = bookingService.getBookingsByUserId(BookingState.PAST,
                1, MyPageRequest.ofSize(1));

        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(LocalDateTime.of(2022, 10, 1, 1, 1),
                result.get(0).getStart());
        Assertions.assertEquals(LocalDateTime.of(2022, 11, 1, 1, 1),
                result.get(0).getEnd());
        Assertions.assertEquals(1, result.get(0).getBooker().getId());
        Assertions.assertEquals(BookingStatus.APPROVED, result.get(0).getStatus());
    }

    @Test
    public void getFutureBookingsByUserIdTest() {
        Booking booking3 = new Booking(1,
                LocalDateTime.of(2022, 12, 1, 1, 1),
                LocalDateTime.of(2022, 12, 30, 1, 1),
                new Item(), user,
                BookingStatus.APPROVED
        );
        when(repository.findAllByBookerIdAndStartAfter(anyLong(), any(LocalDateTime.class),
                any(Sort.class), any(MyPageRequest.class)))
                .thenReturn(List.of(booking3));

        List<BookingOutput> result = bookingService.getBookingsByUserId(BookingState.FUTURE,
                1, MyPageRequest.ofSize(1));

        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 1, 1, 1),
                result.get(0).getStart());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 30, 1, 1),
                result.get(0).getEnd());
        Assertions.assertEquals(1, result.get(0).getBooker().getId());
        Assertions.assertEquals(BookingStatus.APPROVED, result.get(0).getStatus());
    }

    @Test
    public void getWaitingBookingsByUserIdTest() {
        when(repository.findAllByBookerIdAndStatusEquals(anyLong(), any(BookingStatus.class),
                any(Sort.class), any(MyPageRequest.class)))
                .thenReturn(List.of(booking2));

        List<BookingOutput> result = bookingService.getBookingsByUserId(BookingState.WAITING,
                1, MyPageRequest.ofSize(1));

        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 1, 1, 1),
                result.get(0).getStart());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 30, 1, 1),
                result.get(0).getEnd());
        Assertions.assertEquals(1, result.get(0).getBooker().getId());
        Assertions.assertEquals(BookingStatus.WAITING, result.get(0).getStatus());
    }

    @Test
    public void getRejectedBookingsByUserIdTest() {
        Booking booking3 = new Booking(1,
                LocalDateTime.of(2022, 12, 1, 1, 1),
                LocalDateTime.of(2022, 12, 10, 1, 1),
                new Item(), user,
                BookingStatus.REJECTED
        );
        when(repository.findAllByBookerIdAndStatusEquals(anyLong(), any(BookingStatus.class),
                any(Sort.class), any(MyPageRequest.class)))
                .thenReturn(List.of(booking3));

        List<BookingOutput> result = bookingService.getBookingsByUserId(BookingState.REJECTED,
                1, MyPageRequest.ofSize(1));

        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 1, 1, 1),
                result.get(0).getStart());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 10, 1, 1),
                result.get(0).getEnd());
        Assertions.assertEquals(1, result.get(0).getBooker().getId());
        Assertions.assertEquals(BookingStatus.REJECTED, result.get(0).getStatus());
    }

    @Test
    public void getCurrentBookingsByUserIdTest() {
        Booking booking4 = new Booking(1,
                LocalDateTime.of(2022, 11, 1, 1, 1),
                LocalDateTime.of(2022, 12, 10, 1, 1),
                new Item(), user,
                BookingStatus.APPROVED
        );

        when(repository.findAllByBookerIdAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class),
                any(LocalDateTime.class), any(Sort.class), any(MyPageRequest.class)))
                .thenReturn(List.of(booking4));

        List<BookingOutput> result = bookingService.getBookingsByUserId(BookingState.CURRENT,
                1, MyPageRequest.ofSize(1));

        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(LocalDateTime.of(2022, 11, 1, 1, 1),
                result.get(0).getStart());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 10, 1, 1),
                result.get(0).getEnd());
        Assertions.assertEquals(1, result.get(0).getBooker().getId());
        Assertions.assertEquals(BookingStatus.APPROVED, result.get(0).getStatus());
    }

}