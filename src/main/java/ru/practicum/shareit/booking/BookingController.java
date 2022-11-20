package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutput;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
                               String stateText, @RequestHeader("X-Sharer-User-Id") long userId,
                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                               @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на поиск бронирования по id пользователя");
        BookingState state = BookingState.fromString(stateText);
        validateBookingState(state, stateText);
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return service.getBookingsByUserId(state, userId, pageRequest);
    }

    @GetMapping("/owner")
    public List<BookingOutput> getBookingItemsByOwnerId(@RequestParam(defaultValue = "ALL", required = false,
                               name = "state") String stateText, @RequestHeader("X-Sharer-User-Id") long userId,
                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                               @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на поиск бронирования по id владельца");
        BookingState state = BookingState.fromString(stateText);
        validateBookingState(state, stateText);
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return service.getBookingItemsByOwnerId(state, userId, pageRequest);
    }

    private void validateBookingDate(BookingDto bookingDto) {
        if (!bookingDto.getStart().isBefore(bookingDto.getEnd())) {
            throw new IllegalArgumentException("Дата начала бронирования должна быть раньше даты окончания");
        }
    }

    private void validateBookingState(BookingState state, String stateText) {
        if (state == null) {
            throw new IllegalArgumentException("Unknown state: " + stateText);
        }
    }
}
