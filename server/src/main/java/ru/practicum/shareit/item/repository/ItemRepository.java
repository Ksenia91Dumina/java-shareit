package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.additions.MyPageRequest;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableEquals(String name,
                                                                                String description, Boolean available, MyPageRequest pageRequest);

    List<Item> findAllByOwnerId(long userId, MyPageRequest pageRequest);

    List<Item> findAllByRequestId(long requestId);

    @Query(nativeQuery = true, value = "SELECT * FROM items WHERE request_id IN (:idList)")
    List<Item> findAllWhereRequestIdIn(List<Long> idList);

}
