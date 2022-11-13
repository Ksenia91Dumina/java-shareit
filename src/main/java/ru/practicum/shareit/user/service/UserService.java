package ru.practicum.shareit.user.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, long userId);

    List<UserDto> getAllUsers(PageRequest pageRequest);

    UserDto getUserById(long userId) throws NotFoundException;

    void deleteUserById(long userId);
}
