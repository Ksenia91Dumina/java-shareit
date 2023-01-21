package ru.practicum.shareit.comment.model;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment toComment(CommentDto commentDto, User user, long itemId) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                user,
                itemId,
                LocalDateTime.now()
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .itemId(comment.getItemId())
                .created(comment.getCreated())
                .build();
    }
}