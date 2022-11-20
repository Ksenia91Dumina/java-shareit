package ru.practicum.shareit.comment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.model.CommentMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapperTest {

    @Test
    public void mapToCommentTest() {
        User user = new User(1L, "Name", "qwer@mail.ru");
        var commentDto = CommentDto.builder()
                .id(1L)
                .text("Comment text")
                .build();

        Comment result = CommentMapper.toComment(commentDto, user, 1L);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Comment text", result.getText());
        Assertions.assertEquals(user.getName(), result.getAuthor().getName());
        Assertions.assertEquals(1L, result.getItemId());
    }

    @Test
    public void toCommentDtoTest() {
        User user = new User(1L, "Name", "qwer@mail.ru");
        var comment = new Comment(1L, "Comment text", user, 1L, LocalDateTime.now());

        CommentDto result = CommentMapper.toCommentDto(comment);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Comment text", result.getText());
        Assertions.assertEquals(user.getName(), result.getAuthorName());
        Assertions.assertEquals(1L, result.getItemId());
    }
}
