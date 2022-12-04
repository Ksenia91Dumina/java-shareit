package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class RequestRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    ItemRequestRepository repository;

    @Autowired
    UserRepository userRepository;

    private final LocalDateTime date = LocalDateTime.of(2022, 1, 1, 1, 1);
    private User user;
    private ItemRequest request1;
    private ItemRequest request2;

    @BeforeEach
    void init() {
        user = userRepository.save(new User(1L, "Name", "mail@mail.ru"));
        entityManager.persist(user);
        request1 = repository.save(new ItemRequest(1L, "Request text", user, date));
        entityManager.persist(request1);
        request2 = repository.save(new ItemRequest(2L, "Request text2", user, date));
        entityManager.persist(request2);
        entityManager.flush();
        entityManager.getEntityManager().getTransaction().commit();
    }

    @AfterEach
    void clean() {
        userRepository.deleteAll();
        repository.deleteAll();
    }

    @Test
    void findAllByRequester_IdNotOrderByCreatedDescTest() {
        List<ItemRequest> requests = repository.findAllByRequester_IdNotOrderByCreatedDesc(user.getId());
        assertThat(requests.size() == 2);
    }

    @Test
    void findAllByRequester_IdOrderByCreatedDescTest() {
        List<ItemRequest> requests = repository.findAllByRequester_IdOrderByCreatedDesc(user.getId());
        assertThat(requests.size() == 2);
    }
}
