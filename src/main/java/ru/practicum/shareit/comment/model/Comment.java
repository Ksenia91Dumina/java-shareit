package ru.practicum.shareit.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments", schema = "public")
public class Comment {

    @Id
    private long id;
    @Column(name = "text", nullable = false, length = 4000)
    private String text;
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    @Column(name = "item_id", nullable = false)
    private long itemId;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
}
