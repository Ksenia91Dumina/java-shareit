package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.MyPageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
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
public class BookingRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    BookingRepository repository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRequestRepository requestRepository;

    @Autowired
    ItemRepository itemRepository;
    private User user;
    private User owner;
    private Booking booking1;
    private Booking booking2;
    private Item item1;
    private Item item2;
    private ItemRequest request;
    private final LocalDateTime date = LocalDateTime.of(2022, 1, 1, 1, 1);
    private final Sort newestFirst = Sort.by(Sort.Direction.DESC, "start");


    @BeforeEach
    void init() {
        user = userRepository.save(new User(1L, "Name", "qwer@mail.ru"));
        owner = userRepository.save(new User(2L, "Name2", "mail@mail.ru"));
        request = requestRepository.save(new ItemRequest(1L, "Request text", user, date));
        item1 = itemRepository.save(new Item(1, "Name_1", "description for item_1",
                true, owner.getId(), request.getId()));
        item2 = itemRepository.save(new Item(2L, "Name_2", "description for item_2",
                true, owner.getId(), request.getId()));

        booking1 = repository.save(new Booking(1L,
                LocalDateTime.of(2022, 10, 1, 1, 1),
                LocalDateTime.of(2022, 11, 1, 1, 1),
                item1, user,
                BookingStatus.APPROVED
        ));
        this.entityManager.persist(booking1);
        booking2 = repository.save(new Booking(1L,
                LocalDateTime.of(2022, 12, 1, 1, 1),
                LocalDateTime.of(2022, 12, 30, 1, 1),
                item2, user,
                BookingStatus.WAITING
        ));
        this.entityManager.persist(booking2);
        entityManager.getEntityManager().getTransaction().commit();
    }

    @Test
    void findAllByBookerIdTest() {
        List<Booking> test = repository.findAllByBookerId(user.getId(), newestFirst, MyPageRequest.ofSize(1));
        assertThat(test.size() == 2);
    }

    @Test
    void findAllByItem_OwnerIdTest() {
        List<Booking> test = repository.findAllByItem_OwnerId(owner.getId(), newestFirst,
                MyPageRequest.ofSize(1));

        assertThat(test.size() == 2);
    }

    @Test
    void findByBookerIdAndItem_IdAndEndBeforeTest() {
        Booking test = repository.findByBookerIdAndItem_IdAndEndBefore(user.getId(), item1.getId(),
                LocalDateTime.now());
        assertThat(booking1.equals(test));
    }

    @Test
    void findAllByItem_OwnerIdAndStartAfterTest() {
        List<Booking> test = repository.findAllByItem_OwnerIdAndStartAfter(owner.getId(),
                LocalDateTime.of(2022, 11, 20, 1, 1), newestFirst,
                MyPageRequest.ofSize(1));
        assertThat(test.size() == 1);
        assertThat(test.get(0).equals(booking2));
    }

    @Test
    void findAllByBookerIdAndStartAfterTest() {
        List<Booking> test = repository.findAllByBookerIdAndStartAfter(user.getId(),
                LocalDateTime.of(2022, 9, 1, 1, 1), newestFirst,
                MyPageRequest.ofSize(1));
        assertThat(test.size() == 2);
        assertThat(test.get(0).equals(booking1));
    }

    @Test
    void findAllByItem_OwnerIdAndEndBeforeTest() {
        List<Booking> test = repository.findAllByItem_OwnerIdAndEndBefore(owner.getId(),
                LocalDateTime.of(2022, 12, 31, 1, 1), newestFirst,
                MyPageRequest.ofSize(1));
        assertThat(test.size() == 2);
    }
}
