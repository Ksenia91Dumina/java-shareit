package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    ItemRepository repository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRequestRepository requestRepository;
    private final LocalDateTime date = LocalDateTime.of(2022, 1, 1, 1, 1);
    private User user1;
    private User user2;
    private ItemRequest request1;
    private ItemRequest request2;
    private Item item1;
    private Item item2;
    private Item item3;

    @BeforeEach
    void init() {
        user1 = userRepository.save(new User(1L, "User1", "email@mail.ru"));
        user2 = userRepository.save(new User(2L, "User2", "email2@mail.ru"));
        request1 = requestRepository.save(new ItemRequest(1L, "Request text", user1, date));
        request2 = requestRepository.save(new ItemRequest(2L, "Request text2", user1, date));
        item1 = repository.save(new Item(1, "Name_1", "description for item_1",
                true, user1.getId(), request1.getId()));
        this.entityManager.persist(item1);
        item2 = repository.save(new Item(2, "Name_2", "description for item_2",
                true, user1.getId(), request1.getId()));
        this.entityManager.persist(item2);
        item3 = repository.save(new Item(3, "Name_3", "description for item_3",
                true, user2.getId(), request2.getId()));
        this.entityManager.persist(item3);
        entityManager.getEntityManager().getTransaction().commit();
    }

    @AfterEach
    void clean() {
        repository.deleteAll();
        userRepository.deleteAll();
        requestRepository.deleteAll();
    }


    @Test
    void findAllByOwnerIdTest() {
        List<Item> test = repository.findAllByOwnerId(user1.getId(), PageRequest.ofSize(1));
        assertThat(test.size() == 2);
    }

    @Test
    void findAllByRequestIdTest() {
        List<Item> test = repository.findAllByRequestId(request1.getId());
        assertThat(test.size() == 2);
    }

    @Test
    void findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableEqualsTest() {
        List<Item> test = repository.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableEquals(
                "name_2", null, true, PageRequest.ofSize(1));
        assertThat(test.size() == 1);
    }
}
