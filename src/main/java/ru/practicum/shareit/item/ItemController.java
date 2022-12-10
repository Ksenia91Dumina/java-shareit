package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.MyPageRequest;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на добавление предмета");
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Validated({Update.class}) @RequestBody ItemDto itemDto, @PathVariable long itemId) {
        log.info("Получен запрос на изменение предмета");
        return itemService.updateItem(itemDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemInfoDto getItemById(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на получение информации по id предмета = " + itemId);
        return itemService.getItemInfoById(itemId, userId);
    }

    @GetMapping
    public List<ItemInfoDto> getItemsInfoByUserId(@RequestHeader("X-Sharer-User-Id") long userId,
                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                    @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на получение списка предметов пользователя с id = " + userId);
        int page = from / size;
        final MyPageRequest pageRequest = MyPageRequest.of(page, size);
        return itemService.getItemsInfoByUserId(userId, pageRequest);
    }

    @GetMapping("/search")
    public List<ItemDto> searchByText(@RequestParam(name = "text") String text,
                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на поиск по тексту");
        if (text.isBlank()) {
            return List.of();
        }
        int page = from / size;
        final MyPageRequest pageRequest = MyPageRequest.of(page, size);
        return itemService.searchByText(text, pageRequest);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable long itemId,
                                 @RequestHeader("X-Sharer-User-Id") long userId,
                                 @Validated({Create.class}) @RequestBody CommentDto commentDto) {
        log.info("Получен запрос на добавление комментария к предмету с id = " + itemId);
        return itemService.addComment(commentDto, userId, itemId);
    }
}

