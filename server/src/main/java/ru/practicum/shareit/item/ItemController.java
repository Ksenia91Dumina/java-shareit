package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.additions.MyPageRequest;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j

public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на добавление предмета");
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody ItemDto itemDto, @PathVariable long itemId) {
        log.info("Получен запрос на изменение предмета");
        return itemService.updateItem(itemDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemInfoDto getItemById(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на получение информации по id предмета = {}", itemId);
        return itemService.getItemInfoById(itemId, userId);
    }

    @GetMapping
    public List<ItemInfoDto> getItemsInfoByUserId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на получение списка предметов пользователя с id = {}", userId);
        int page = from / size;
        final MyPageRequest pageRequest = MyPageRequest.of(page, size);
        return itemService.getItemsInfoByUserId(userId, pageRequest);
    }

    @GetMapping("/search")
    public List<ItemDto> searchByText(@RequestParam(name = "text") String text,
                                      @RequestParam(name = "from", defaultValue = "0") Integer from,
                                      @RequestParam(name = "size", defaultValue = "10") Integer size) {
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
                                 @RequestBody CommentDto commentDto) {
        log.info("Получен запрос на добавление комментария к предмету с id = {}", itemId);
        return itemService.addComment(commentDto, userId, itemId);
    }
}

