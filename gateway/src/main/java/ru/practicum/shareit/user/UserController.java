package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.additions.Create;
import ru.practicum.shareit.additions.MyPageRequest;
import ru.practicum.shareit.additions.Update;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@Validated({Create.class}) @RequestBody UserDto userDto) {
        log.info("Получен запрос на добавление пользователя");
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@Validated({Update.class}) @RequestBody UserDto userDto,
                                         @PathVariable long userId) {
        log.info("Получен запрос на изменение пользователя");
        return userClient.updateUser(userDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                              Integer from, @Positive @RequestParam(name = "size", defaultValue = "10")
                                              Integer size) {
        log.info("Получен запрос на получение списка всех пользователей");
        int page = from / size;
        final MyPageRequest pageRequest = MyPageRequest.of(page, size);
        return userClient.getAllUsers(pageRequest);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable long userId) {
        log.info("Получен запрос на получение информации по id пользователя = " + userId);
        return userClient.getUserById(userId);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable long userId) {
        log.info("Получен запрос на удаление пользователя");
        return userClient.deleteUserById(userId);
    }
}
