package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.additions.MyPageRequest;
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
@Validated
public class BookingController {
    private final BookingService service;

    @PostMapping
    public BookingOutput addBooking(@RequestBody BookingDto bookingDto,
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
                                                   @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на поиск бронирования по id пользователя");
        BookingState state = BookingState.fromString(stateText);
        validateBookingState(state, stateText);
        checkingParamsForPagination(from, size);
        final MyPageRequest pageRequest = new MyPageRequest(from, size, Sort.unsorted());
        return service.getBookingsByUserId(state, userId, pageRequest);
    }

    @GetMapping("/owner")
    public List<BookingOutput> getBookingItemsByOwnerId(@RequestParam(defaultValue = "ALL", required = false,
            name = "state") String stateText, @RequestHeader("X-Sharer-User-Id") long userId,
                                                       @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                       @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на поиск бронирования по id владельца");
        BookingState state = BookingState.fromString(stateText);
        checkingParamsForPagination(from, size);
        validateBookingState(state, stateText);
        final MyPageRequest pageRequest = new MyPageRequest(from, size, Sort.unsorted());
        return service.getBookingItemsByOwnerId(state, userId, pageRequest);
    }

    private void validateBookingDate(BookingDto bookingDto) {
        if (!bookingDto.getStart().isBefore(bookingDto.getEnd())) {
            throw new ValidateException("Дата начала бронирования должна быть раньше даты окончания");
        }
    }

    public void validateBookingState(BookingState state, String stateText) {
        if (state == null) {
            throw new ValidateException("Unknown state: " + stateText);
        }
    }

    private void checkingParamsForPagination(int from, int size) {
        if (from > size) {
            throw new IllegalArgumentException("Параметр from должен быть меньше размера страницы");
        }
    }

}
