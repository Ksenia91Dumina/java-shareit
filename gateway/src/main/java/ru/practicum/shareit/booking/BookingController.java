package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.additions.Create;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.ValidateException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @Validated({Create.class}) @RequestBody BookItemRequestDto bookingDto) {
        log.info("Получен запрос на добавление бронирования");
        validateBookingDate(bookingDto);
        return bookingClient.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @PathVariable long bookingId,
                                                @RequestParam boolean approved) {
        log.info("Получен запрос на подтверждение бронирования");
        return bookingClient.updateBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long bookingId) {
        log.info("Получен запрос на поиск информации по id  = {} бронирования", bookingId);
        return bookingClient.getBookingById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByUserId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @RequestParam(name = "state", defaultValue = "ALL") String stateText,
                                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                      @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на поиск бронирования по id пользователя = {}", userId);
        BookingState state = BookingState.from(stateText)
                .orElseThrow(() -> new IllegalArgumentException(stateText + " не существует"));
        validateBookingState(state, stateText);
        return bookingClient.getBookingsByUserId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingItemsByOwnerId(@RequestParam(defaultValue = "ALL", name = "state")
                                                           String stateText,
                                                           @RequestHeader("X-Sharer-User-Id") long userId,
                                                           @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                           @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на поиск бронирования по id владельца");
        BookingState state = BookingState.from(stateText)
                .orElseThrow(() -> new IllegalArgumentException(stateText + " не существует"));
        validateBookingState(state, stateText);
        return bookingClient.getBookingItemsByOwnerId(state, userId, from, size);
    }

    private void validateBookingDate(BookItemRequestDto bookingDto) {
        if (!bookingDto.getStart().isBefore(bookingDto.getEnd())) {
            throw new ValidateException("Дата начала бронирования должна быть раньше даты окончания");
        }
    }

    public void validateBookingState(BookingState state, String stateText) {
        if (state == null) {
            throw new ValidateException(String.format("Unknown state: %s", stateText));
        }
    }

}

