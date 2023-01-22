package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.additions.Create;
import ru.practicum.shareit.additions.MyPageRequest;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.ValidateException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(@Validated({Create.class}) @RequestBody BookItemRequestDto bookingDto,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на добавление бронирования");
        validateBookingDate(bookingDto);
        return bookingClient.addBooking(userId, bookingDto);

    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@PathVariable long bookingId,
                                                @RequestParam boolean approved,
                                                @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на подтверждение бронирования");
        return bookingClient.updateBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на поиск информации по id бронирования");
        return bookingClient.getBookingById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByUserId(@RequestParam(name = "state", defaultValue = "ALL", required = false)
                                                      String stateText, @RequestHeader("X-Sharer-User-Id") long userId,
                                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                      @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на поиск бронирования по id пользователя");
        BookingState state = BookingState.from(stateText)
                .orElseThrow(() -> new IllegalArgumentException(stateText + " не существует"));
        validateBookingState(state, stateText);
        checkingParamsForPagination(from, size);
        final MyPageRequest pageRequest = new MyPageRequest(from, size, Sort.unsorted());
        return bookingClient.getBookingsByUserId(state, userId, pageRequest);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingItemsByOwnerId(@RequestParam(defaultValue = "ALL", required = false,
            name = "state") String stateText, @RequestHeader("X-Sharer-User-Id") long userId,
                                                           @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                           @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на поиск бронирования по id владельца");
        BookingState state = BookingState.from(stateText)
                .orElseThrow(() -> new IllegalArgumentException(stateText + " не существует"));
        checkingParamsForPagination(from, size);
        validateBookingState(state, stateText);
        final MyPageRequest pageRequest = new MyPageRequest(from, size, Sort.unsorted());
        return bookingClient.getBookingItemsByOwnerId(state, userId, pageRequest);
    }

    private void validateBookingDate(BookItemRequestDto bookingDto) {
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

