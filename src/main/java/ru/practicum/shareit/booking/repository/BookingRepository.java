package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.MyPageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(long id, Sort sort, MyPageRequest pageRequest);

    List<Booking> findAllByItem_OwnerId(long id, Sort sort, MyPageRequest pageRequest);

    Booking findByBookerIdAndItem_IdAndEndBefore(long userId, long itemId, LocalDateTime time);

    List<Booking> findAllByItem_OwnerIdAndStartAfter(long id, LocalDateTime start, Sort sort, MyPageRequest pageRequest);

    List<Booking> findAllByBookerIdAndStartAfter(long id, LocalDateTime start, Sort sort, MyPageRequest pageRequest);


    List<Booking> findAllByItem_OwnerIdAndEndBefore(long id, LocalDateTime start, Sort sort, MyPageRequest pageRequest);

    List<Booking> findAllByBookerIdAndEndBefore(long id, LocalDateTime start, Sort sort, MyPageRequest pageRequest);

    List<Booking> findAllByItem_OwnerIdAndStartBeforeAndEndAfter(long id, LocalDateTime start,
                                                                 LocalDateTime end, Sort sort, MyPageRequest pageRequest);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(long id, LocalDateTime start,
                                                             LocalDateTime end, Sort sort, MyPageRequest pageRequest);

    List<Booking> findAllByItem_OwnerIdAndStatusEquals(long id, BookingStatus status, Sort sort,
                                                       MyPageRequest pageRequest);

    List<Booking> findAllByBookerIdAndStatusEquals(long id, BookingStatus status, Sort sort, MyPageRequest pageRequest);

    Booking findByItem_IdAndEndBeforeAndStatusNot(long itemId, LocalDateTime end, BookingStatus status, Sort sort);

    Booking findByItem_IdAndStatusAndStartAfter(long itemId, BookingStatus status, LocalDateTime start, Sort sort);
}
