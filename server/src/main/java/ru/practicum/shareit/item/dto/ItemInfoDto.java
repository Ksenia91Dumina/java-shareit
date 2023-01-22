package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.Set;

@Data
@Builder
public class ItemInfoDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long ownerId;
    private Long requestId;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private Set<CommentDto> comments;

    public ItemInfoDto(long id, String name, String description, Boolean available, long ownerId, Long requestId,
                       BookingDto lastBooking, BookingDto nextBooking, Set<CommentDto> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.ownerId = ownerId;
        this.requestId = requestId;
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
        this.comments = comments;
    }
}
