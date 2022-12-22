package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingOutput;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class BookingModelTest {

    private final Booking booking = new Booking(
            1,
            LocalDateTime.of(2022, 11, 1, 1, 1),
            LocalDateTime.of(2022, 12, 1, 1, 1),
            new Item(1, "Item1", "Description1", true, 1L, 1L),
            new User(1, "Name", "qwer@mail.ru"),
            BookingStatus.APPROVED);

    private final Booking booking2 = new Booking(
            2,
            LocalDateTime.of(2022, 12, 1, 1, 1),
            LocalDateTime.of(2022, 12, 20, 1, 1),
            new Item(2, "Item2", "Description2", true, 2L, 2L),
            new User(2, "Name2", "asdf@mail.ru"),
            BookingStatus.WAITING);

    @Test
    public void objectEqualsTest() {
        BookingOutput bookingOutput1 = BookingMapper.toBookingOutput(booking);
        BookingOutput bookingOutput2 = BookingMapper.toBookingOutput(booking);
        Assertions.assertTrue(bookingOutput1.equals(bookingOutput2) &&
                bookingOutput2.equals(bookingOutput1));
        Assertions.assertEquals(bookingOutput1.hashCode(), bookingOutput2.hashCode());
        Assertions.assertTrue(bookingOutput1.getBooker().equals(bookingOutput2.getBooker()) &&
                bookingOutput2.getBooker().equals(bookingOutput1.getBooker()));
        Assertions.assertEquals(bookingOutput1.getBooker().hashCode(), bookingOutput2.getBooker().hashCode());
        Assertions.assertTrue(bookingOutput1.getItem().equals(bookingOutput2.getItem()) &&
                bookingOutput2.getItem().equals(bookingOutput1.getItem()));
        Assertions.assertEquals(bookingOutput1.getItem().hashCode(), bookingOutput2.getItem().hashCode());
    }

    @Test
    public void objectNotEqualsTest() {
        BookingOutput bookingOutput1 = BookingMapper.toBookingOutput(booking);
        BookingOutput bookingOutput2 = BookingMapper.toBookingOutput(booking2);
        Assertions.assertFalse(bookingOutput1.equals(bookingOutput2) &&
                bookingOutput2.equals(bookingOutput1));
        Assertions.assertNotEquals(bookingOutput1.hashCode(), bookingOutput2.hashCode());
        Assertions.assertFalse(bookingOutput1.getBooker().equals(bookingOutput2.getBooker()) &&
                bookingOutput2.getBooker().equals(bookingOutput1.getBooker()));
        Assertions.assertNotEquals(bookingOutput1.getBooker().hashCode(), bookingOutput2.getBooker().hashCode());
        Assertions.assertFalse(bookingOutput1.getItem().equals(bookingOutput2.getItem()) &&
                bookingOutput2.getItem().equals(bookingOutput1.getItem()));
        Assertions.assertNotEquals(bookingOutput1.getItem().hashCode(), bookingOutput2.getItem().hashCode());
    }
}
