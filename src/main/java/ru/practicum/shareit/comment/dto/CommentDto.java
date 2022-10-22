package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {

    private long id;
    @NotNull(groups = {Create.class})
    @NotEmpty(groups = {Create.class})
    private String text;
    private String authorName;
    private long itemId;
    private LocalDateTime created;
}