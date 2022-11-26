package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutput;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Slf4j
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
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на получение списка запросов пользователя с id = " + userId);
        validateParams(from, size);
        return itemRequestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestOutput getRequestById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long requestId) {
        log.info("Получен запрос на получение информации по id  = " + requestId);
        return itemRequestService.getRequestById(requestId, userId);
    }

    private void validateParams(int from, Integer size) {
        if (from < 0) {
            throw new IllegalArgumentException("Parameter from must be => 0");
        }
        if (size != null) {
            if (size <= 0) {
                throw new IllegalArgumentException("Parameter size must be > 0");
            }
        }
    }

}
