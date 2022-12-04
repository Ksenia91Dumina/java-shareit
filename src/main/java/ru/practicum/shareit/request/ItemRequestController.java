package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutput;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createRequest(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @Validated({Create.class}) @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Получен запрос на добавление запроса");
        return itemRequestService.createRequest(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestOutput> getRequestsByUserId(
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на получение информации по id пользователя = " + userId);
        return itemRequestService.getRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestOutput> getRequests(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на получение списка запросов пользователя с id = " + userId);
        return itemRequestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestOutput getRequestById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long requestId) {
        log.info("Получен запрос на получение информации по id  = " + requestId);
        return itemRequestService.getRequestById(requestId, userId);
    }


}
