package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableEquals(String name,
                                                                                String description, Boolean available);

    List<Item> findAllByOwnerId(long userId);

    List<Item> findAllByRequestId(long requestId);
}
