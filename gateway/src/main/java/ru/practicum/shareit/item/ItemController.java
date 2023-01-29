package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.additions.Create;
import ru.practicum.shareit.additions.MyPageRequest;
import ru.practicum.shareit.additions.Update;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на добавление предмета");
        return itemClient.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @Validated({Update.class}) @RequestBody ItemDto itemDto,
                                             @PathVariable long itemId) {
        log.info("Получен запрос на изменение предмета");
        return itemClient.updateItem(itemDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable long itemId,
                                              @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(String.format("Получен запрос на получение информации по id предмета = %s", itemId));
        return itemClient.getItemInfoById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsInfoByUserId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                       @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                       @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info(String.format("Получен запрос на получение списка предметов пользователя с id = %s", userId));
        int page = from / size;
        final MyPageRequest pageRequest = MyPageRequest.of(page, size);
        return itemClient.getItemsInfoByUserId(userId, pageRequest);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchByText(@RequestParam(name = "text") String text,
                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                               @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на поиск по тексту");
        int page = from / size;
        final MyPageRequest pageRequest = MyPageRequest.of(page, size);
        return itemClient.searchByText(text, pageRequest);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable long itemId,
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Validated({Create.class}) @RequestBody CommentDto commentDto) {
        log.info(String.format("Получен запрос на добавление комментария к предмету с id = %s", itemId));
        return itemClient.addComment(commentDto, userId, itemId);
    }
}


