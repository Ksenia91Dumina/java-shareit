package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutput;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {

    BookingOutput addBooking(BookingDto bookingDto, long userId);

    BookingOutput updateBooking(long bookingId, boolean approved, long userId);

    BookingOutput getBookingById(long bookingId, long userId);

    List<BookingOutput> getBookingsByUserId(BookingState state, long userId, PageRequest pageRequest);

    List<BookingOutput> getBookingItemsByOwnerId(BookingState state, long userId, PageRequest pageRequest);
}