package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {

    private long id;
    @NotBlank(groups = {Create.class})
    private String text;
    private String authorName;
    private long itemId;
    private LocalDateTime created;
}