package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.additions.Create;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class BookingDto {

    private long id;
    @NotNull(groups = {Create.class})
    @FutureOrPresent(groups = {Create.class})
    private LocalDateTime start;
    @NotNull(groups = {Create.class})
    @Future(groups = {Create.class})
    private LocalDateTime end;
    @NotNull(groups = {Create.class})
    private long itemId;
    @NotNull(groups = {Create.class})
    private long bookerId;
    private BookingStatus status;

    public BookingDto(long id, LocalDateTime start, LocalDateTime end,
                      long itemId, long bookerId, BookingStatus status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.itemId = itemId;
        this.bookerId = bookerId;
        this.status = status;
    }
}
