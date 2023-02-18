package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class CommentDto {

    private long id;

    private String text;
    private String authorName;
    private long itemId;
    private LocalDateTime created;

    public CommentDto(long id, String text, String authorName, long itemId, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.authorName = authorName;
        this.itemId = itemId;
        this.created = created;
    }
}