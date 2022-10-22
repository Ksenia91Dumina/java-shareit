package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {

    private long id;
    @NotNull(groups = {Create.class})
    @FutureOrPresent(groups = {Update.class, Create.class})
    private LocalDateTime start;
    @NotNull(groups = {Create.class})
    @Future(groups = {Update.class, Create.class})
    private LocalDateTime end;
    @NotNull(groups = {Update.class, Create.class})
    private long itemId;
    @NotNull(groups = {Update.class, Create.class})
    private long bookerId;
    private BookingStatus status;
}
