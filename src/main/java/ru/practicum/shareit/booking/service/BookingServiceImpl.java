package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    @Autowired
    public BookingRepository bookingRepository;

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
    public List<BookingOutput> getBookingsByUserId(BookingState state, long userId, PageRequest pageRequest) {
        userService.getUserById(userId);
        Sort newestFirst = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> bookings = bookingRepository.findAll();
        switch (state) {
            case ALL: {
                bookings = bookingRepository.findAllByBookerId(userId, newestFirst, pageRequest);
                break;
            }
            case CURRENT: {
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(userId,
                        LocalDateTime.now(), LocalDateTime.now(), newestFirst, pageRequest);
                break;
            }
            case FUTURE: {
                bookings = bookingRepository.findAllByBookerIdAndStartAfter(userId,
                        LocalDateTime.now(), newestFirst, pageRequest);
                break;
            }
            case PAST: {
                bookings = bookingRepository.findAllByBookerIdAndEndBefore(userId,
                        LocalDateTime.now(), newestFirst, pageRequest);
                break;
            }
            case WAITING: {
                bookings = bookingRepository.findAllByBookerIdAndStatusEquals(userId,
                        BookingStatus.WAITING, newestFirst, pageRequest);
                break;
            }
            case REJECTED: {
                bookings = bookingRepository.findAllByBookerIdAndStatusEquals(userId,
                        BookingStatus.REJECTED, newestFirst, pageRequest);
                break;
            }

        }
        return bookings.stream()
                .map(BookingMapper::toBookingOutput)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingOutput> getBookingItemsByOwnerId(BookingState state, long userId, PageRequest pageRequest) {
        userService.getUserById(userId);
        Sort newestFirst = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> bookings = bookingRepository.findAll();
        switch (state) {
            case ALL: {
                bookings = bookingRepository.findAllByItem_OwnerId(userId, newestFirst, pageRequest);
                break;
            }
            case CURRENT: {
                bookings = bookingRepository.findAllByItem_OwnerIdAndStartBeforeAndEndAfter(userId,
                        LocalDateTime.now(), LocalDateTime.now(), newestFirst, pageRequest);
                break;
            }
            case FUTURE: {
                bookings = bookingRepository.findAllByItem_OwnerIdAndStartAfter(userId,
                        LocalDateTime.now(), newestFirst, pageRequest);
                break;
            }
            case PAST: {
                bookings = bookingRepository.findAllByItem_OwnerIdAndEndBefore(userId,
                        LocalDateTime.now(), newestFirst, pageRequest);
                break;
            }
            case WAITING: {
                bookings = bookingRepository.findAllByItem_OwnerIdAndStatusEquals(userId,
                        BookingStatus.WAITING, newestFirst, pageRequest);
                break;
            }
            case REJECTED: {
                bookings = bookingRepository.findAllByItem_OwnerIdAndStatusEquals(userId,
                        BookingStatus.REJECTED, newestFirst, pageRequest);
                break;
            }

        }
        return bookings.stream()
                .map(BookingMapper::toBookingOutput)
                .collect(Collectors.toList());
    }

}
