package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.additions.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class CommentDto {

    private long id;
    @NotBlank(groups = {Create.class})
    @Size(max = 4000)
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