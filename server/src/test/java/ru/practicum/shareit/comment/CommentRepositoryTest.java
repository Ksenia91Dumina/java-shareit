package ru.practicum.shareit.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    CommentRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemRequestRepository requestRepository;

    @Test
    void testExample() throws Exception {
        LocalDateTime date = LocalDateTime.of(2022, 1, 1, 1, 1);
        User user1 = userRepository.save(new User(1L, "User1", "email@mail.ru"));
        this.entityManager.persist(user1);
        User user2 = userRepository.save(new User(2L, "User2", "email2@mail.ru"));
        this.entityManager.persist(user2);
        ItemRequest request = requestRepository.save(new ItemRequest(1L, "Request text", user1, date));
        this.entityManager.persist(request);
        Item item = itemRepository.save(new Item(1L, "Name_1", "description for item_1",
                true, 1, request.getId()));
        this.entityManager.persist(item);
        Comment comment1 = repository.save(new Comment(1L, "text1", user1,
                item.getId(), LocalDateTime.of(2022, 12, 1, 1, 1)));
        this.entityManager.persist(comment1);
        Comment comment2 = repository.save(new Comment(2L, "text2", user2,
                item.getId(), LocalDateTime.of(2022, 12, 1, 1, 1)));
        this.entityManager.persist(comment2);
        entityManager.getEntityManager().getTransaction().commit();
        Set<Comment> test = repository.findAllByItemId(1L);
        assertThat(test.size() == 2);
    }
}
