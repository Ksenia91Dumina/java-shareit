package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutput;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    @Transactional
    public BookingOutput addBooking(BookingDto bookingDto, long userId) {
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(UserMapper.toUser(userService.getUserById(userId)));
        Item item = itemService.getItemById(bookingDto.getItemId());
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        Item itemToCheck = itemService.getItemById(booking.getItem().getId());
        if (itemToCheck.getOwnerId() != userId) {
            if (itemToCheck.getAvailable()) {
                return BookingMapper.toBookingOutput(bookingRepository.save(booking));
            } else {
                throw new ValidateException("Предмет с id = " + item.getId() + " недоступен для бронирования");
            }
        } else {
            throw new NotFoundException("Пользователь с id = " + userId +
                    " не может забронировать предмет с id = " + item.getId());
        }
    }

    @Override
    @Transactional
    public BookingOutput updateBooking(long bookingId, boolean approved, long userId) {
        userService.getUserById(userId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Бронирование с id = " + bookingId + " не найдено"));
        Item item = itemService.getItemById(booking.getItem().getId());
        if (item.getOwnerId() == userId) {
            if (approved) {
                if (booking.getStatus().equals(BookingStatus.APPROVED)) {
                    throw new ValidateException("Статус бронирования уже = APPROVED");
                } else {
                    booking.setStatus(BookingStatus.APPROVED);
                }
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }
            return BookingMapper.toBookingOutput(bookingRepository.save(booking));
        } else {
            throw new NotFoundException("Пользователь с id = " + userId +
                    " не может внести изменение в бронирование");
        }
    }

    @Override
    public BookingOutput getBookingById(long bookingId, long userId) {
        userService.getUserById(userId);
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isPresent()) {
            Item item = itemService.getItemById(booking.get().getItem().getId());
            if (booking.get().getBooker().getId() == userId || item.getOwnerId() == userId) {
                return BookingMapper.toBookingOutput(booking.get());
            } else {
                throw new NotFoundException("Пользователь с id = " + userId +
                        " не может внести изменение в бронирование");
            }
        } else {
            throw new NotFoundException("Бронирование с id = " + bookingId + " не найдено");
        }
    }

    @Override
    public List<BookingOutput> getBookingsByUserId(BookingState state, long userId) {
        userService.getUserById(userId);
        List<Booking> bookings = bookingRepository.findAllByBookerId(userId,
                Sort.by(Sort.Direction.DESC, "start"));
        return filterByState(bookings, state);
    }

    @Override
    public List<BookingOutput> getBookingItemsByOwnerId(BookingState state, long userId) {
        userService.getUserById(userId);
        List<Booking> bookings = bookingRepository.findAllByItem_OwnerId(userId,
                Sort.by(Sort.Direction.DESC, "start"));
        return filterByState(bookings, state);
    }

    private List<BookingOutput> filterByState(List<Booking> bookings, BookingState state) {
        if (state.equals(BookingState.CURRENT)) {
            return bookings.stream()
                    .filter(booking -> booking.getStart().isBefore(LocalDateTime.now())
                            && booking.getEnd().isAfter(LocalDateTime.now()))
                    .map(BookingMapper::toBookingOutput)
                    .collect(Collectors.toList());
        } else if (state.equals(BookingState.FUTURE)) {
            return bookings.stream()
                    .filter(booking -> booking.getStart().isAfter(LocalDateTime.now())
                            && !booking.getStatus().equals(BookingStatus.REJECTED))
                    .map(BookingMapper::toBookingOutput)
                    .collect(Collectors.toList());
        } else if (state.equals(BookingState.PAST)) {
            return bookings.stream()
                    .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                    .map(BookingMapper::toBookingOutput)
                    .collect(Collectors.toList());
        } else if (state.equals(BookingState.WAITING)) {
            return bookings.stream()
                    .filter(booking -> booking.getStatus().equals(BookingStatus.WAITING))
                    .map(BookingMapper::toBookingOutput)
                    .collect(Collectors.toList());
        } else if (state.equals(BookingState.REJECTED)) {
            return bookings.stream()
                    .filter(booking -> booking.getStatus().equals(BookingStatus.REJECTED))
                    .map(BookingMapper::toBookingOutput)
                    .collect(Collectors.toList());
        } else {
            return bookings.stream()
                    .map(BookingMapper::toBookingOutput)
                    .collect(Collectors.toList());
        }
    }
}
