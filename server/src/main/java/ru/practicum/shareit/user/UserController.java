package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.additions.MyPageRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j

public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        log.info("Получен запрос на добавление пользователя");
        return userService.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable long userId) {
        log.info("Получен запрос на изменение пользователя");
        return userService.updateUser(userDto, userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                     @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на получение списка всех пользователей");
        int page = from / size;
        final MyPageRequest pageRequest = MyPageRequest.of(page, size);
        return userService.getAllUsers(pageRequest);
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable long userId) {
        log.info("Получен запрос на получение информации по id пользователя = " + userId);
        return userService.getUserById(userId);
    }

    @DeleteMapping("{userId}")
    public void deleteUserById(@PathVariable long userId) {
        log.info("Получен запрос на удаление пользователя");
        userService.deleteUserById(userId);
    }
}

