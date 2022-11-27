package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutput;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private final BookingDto bookingDto = BookingDto.builder()
            .id(1L)
            .start(LocalDateTime.of(2022, 12, 1, 1, 1))
            .end(LocalDateTime.of(2023, 1, 1, 1, 1))
            .bookerId(1L)
            .itemId(1L)
            .build();

    private final BookingOutput bookingOutput = BookingOutput.builder()
            .id(2L)
            .start(LocalDateTime.of(2022, 12, 1, 1, 1))
            .end(LocalDateTime.of(2023, 2, 2, 2, 2))
            .item(new BookingOutput.Item(new Item(1L, "Item_1", "description for item_1",
                    true, 1L, null)))
            .booker(new BookingOutput.Booker(new User(1L, "Name", "qwer@mail.ru")))
            .status(BookingStatus.WAITING)
            .build();

    @Test
    void addBookingTest() throws Exception {
        when(bookingService.addBooking(any(BookingDto.class), anyLong()))
                .thenReturn(bookingOutput);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingOutput.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(String.valueOf(bookingOutput.getStatus()))))
                .andExpect(jsonPath("$.booker", is(bookingOutput.getBooker()), BookingOutput.Booker.class))
                .andExpect(jsonPath("$.item", is(bookingOutput.getItem()), BookingOutput.Item.class));
    }

    @Test
    void updateBookingTest() throws Exception {
        when(bookingService.updateBooking(anyLong(), anyBoolean(), anyLong()))
                .thenReturn(bookingOutput);

        mvc.perform(patch("/bookings/1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", String.valueOf(true)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingOutput.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(String.valueOf(bookingOutput.getStatus()))))
                .andExpect(jsonPath("$.booker", is(bookingOutput.getBooker()),
                        BookingOutput.Booker.class))
                .andExpect(jsonPath("$.item", is(bookingOutput.getItem()), BookingOutput.Item.class));
    }

    @Test
    void getBookingByIdTest() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingOutput);

        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingOutput.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(String.valueOf(bookingOutput.getStatus()))))
                .andExpect(jsonPath("$.booker", is(bookingOutput.getBooker()),
                        BookingOutput.Booker.class))
                .andExpect(jsonPath("$.item", is(bookingOutput.getItem()), BookingOutput.Item.class));
    }

    @Test
    void getBookingItemsByOwnerIdTest() throws Exception {
        when(bookingService.getBookingItemsByOwnerId(any(BookingState.class),
                anyLong(), any(PageRequest.class)))
                .thenReturn(List.of(bookingOutput));

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingOutput.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(String.valueOf(bookingOutput.getStatus()))))
                .andExpect(jsonPath("$[0].booker", is(bookingOutput.getBooker()),
                        BookingOutput.Booker.class))
                .andExpect(jsonPath("$[0].item", is(bookingOutput.getItem()), BookingOutput.Item.class));
    }

    @Test
    void addWrongBookingTest() throws Exception {
        BookingDto bookingDto2 = BookingDto.builder()
                .id(2L)
                .start(LocalDateTime.of(2022, 12, 1, 1, 1))
                .end(LocalDateTime.of(2022, 1, 1, 1, 1))
                .bookerId(1L)
                .itemId(1L)
                .build();
        when(bookingService.addBooking(any(BookingDto.class), anyLong()))
                .thenThrow(new ValidateException("Дата начала бронирования должна быть раньше даты окончания"));

        final ValidateException exception = Assertions.assertThrows(
                ValidateException.class,
                () -> bookingService.addBooking(bookingDto2, 1L));

        Assertions.assertEquals("Дата начала бронирования должна быть раньше даты окончания",
                exception.getMessage());

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error", is(exception.getMessage())));
    }

    @Test
    void validateStateTest() {
        BookingController controller = new BookingController(bookingService);
        Assertions.assertThrows(ValidateException.class, () ->
                controller.validateBookingState(null, "NewState"));
    }
}
