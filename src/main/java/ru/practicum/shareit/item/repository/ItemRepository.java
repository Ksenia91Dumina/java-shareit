package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.additions.MyPageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableEquals(String name,
                        String description, Boolean available, MyPageRequest pageRequest);

    List<Item> findAllByOwnerId(long userId, MyPageRequest pageRequest);

    List<Item> findAllByRequestId(long requestId);

    List<Item> findAllWhereRequest_IdIn(List<ItemRequest> requests);

}
