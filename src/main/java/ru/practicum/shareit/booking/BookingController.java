package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutput;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ValidateException;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService service;

    @PostMapping
    public BookingOutput addBooking(@Validated({Create.class}) @RequestBody BookingDto bookingDto,
                                    @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на добавление бронирования");
        validateBookingDate(bookingDto);
        return service.addBooking(bookingDto, userId);

    }

    @PatchMapping("/{bookingId}")
    public BookingOutput updateBooking(@PathVariable long bookingId,
                                       @RequestParam boolean approved,
                                       @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на подтверждение бронирования");
        return service.updateBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingOutput getBooking(@PathVariable long bookingId,
                                    @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на поиск информации по id бронирования");
        return service.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingOutput> getBookingsByUserId(@RequestParam(name = "state", defaultValue = "ALL", required = false)
                                                   String stateText, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на поиск бронирования по id пользователя");
        BookingState state = BookingState.fromString(stateText);
        validateBookingState(state, stateText);
        return service.getBookingsByUserId(state, userId);
    }

    @GetMapping("/owner")
    public List<BookingOutput> getBookingItemsByOwnerId(@RequestParam(defaultValue = "ALL", required = false, name = "state")
                                               String stateText, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на поиск бронирования по id владельца");
        BookingState state = BookingState.fromString(stateText);
        validateBookingState(state, stateText);
        return service.getBookingItemsByOwnerId(state, userId);
    }

    private void validateBookingDate(BookingDto bookingDto) {
        if (!bookingDto.getStart().isBefore(bookingDto.getEnd())) {
            throw new ValidateException("Дата начала бронирования должна быть раньше даты окончания");
        }
    }

    private void validateBookingState(BookingState state, String stateText) {
        if (state == null) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
    }
}
