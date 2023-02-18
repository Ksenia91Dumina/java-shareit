package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutput;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class BookingMapperTest {

    @Test
    public void mapToBookingTest() {
        var bookingDto = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.of(2022, 11, 1, 1, 1))
                .end(LocalDateTime.of(2022, 12, 1, 1, 1))
                .bookerId(1)
                .itemId(1)
                .status(BookingStatus.APPROVED)
                .build();

        Booking result = BookingMapper.toBooking(bookingDto);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(LocalDateTime.of(2022, 11, 1, 1, 1), result.getStart());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 1, 1, 1), result.getEnd());
        Assertions.assertNull(result.getBooker());
        Assertions.assertNull(result.getItem());
        Assertions.assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    public void mapToBookingDtoTest() {
        var booking = new Booking(
                2,
                LocalDateTime.of(2022, 11, 1, 1, 1),
                LocalDateTime.of(2022, 12, 1, 1, 1),
                new Item(),
                new User(),
                BookingStatus.APPROVED
        );

        BookingDto result = BookingMapper.toBookingDto(booking);

        Assertions.assertEquals(2, result.getId());
        Assertions.assertEquals(LocalDateTime.of(2022, 11, 1, 1, 1), result.getStart());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 1, 1, 1), result.getEnd());
        Assertions.assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    public void mapToBookingOutputTest() {
        var booking = new Booking(
                3,
                LocalDateTime.of(2022, 11, 1, 1, 1),
                LocalDateTime.of(2022, 12, 1, 1, 1),
                new Item(),
                new User(1, "Name", "qwer@mail.ru"),
                BookingStatus.APPROVED
        );

        BookingOutput result = BookingMapper.toBookingOutput(booking);

        Assertions.assertEquals(3, result.getId());
        Assertions.assertEquals(LocalDateTime.of(2022, 11, 1, 1, 1), result.getStart());
        Assertions.assertEquals(LocalDateTime.of(2022, 12, 1, 1, 1), result.getEnd());
        Assertions.assertEquals(1, result.getBooker().getId());
        Assertions.assertEquals(BookingStatus.APPROVED, result.getStatus());
    }
}
