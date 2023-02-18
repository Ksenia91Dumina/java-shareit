package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.additions.MyPageRequest;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotAllowedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        try {
            User user = UserMapper.toUser(userDto);
            return UserMapper.toUserDto(userRepository.save(user));
        } catch (Exception e) {
            throw new DuplicateEmailException(String.format("Пользователь с почтой %s уже существует",
                    userDto.getEmail()));
        }
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, long userId) {
        checkUserEmailForDuplicate(userDto.getEmail());
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь с id = %s не найден", userId)));
        if (user.getId() == userId) {
            User userToUpdate = UserMapper.toUser(userDto);
            userToUpdate.setId(userId);
            if (userToUpdate.getName() != null && !userToUpdate.getName().isBlank()) {
                user.setName(userToUpdate.getName());
            }
            if (userToUpdate.getEmail() != null && !userToUpdate.getEmail().isBlank()) {
                user.setEmail(userToUpdate.getEmail());
            }
            return UserMapper.toUserDto(user);
        } else {
            throw new NotAllowedException(String.format("Пользователь с id = %s не может изменить пользователя с id = %s",
                    user.getId(), userId));
        }
    }

    @Override
    public List<UserDto> getAllUsers(MyPageRequest pageRequest) {
        return userRepository.findAll(pageRequest)
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь с id = %s не найден", userId)));
        return UserMapper.toUserDto(user);
    }


    @Override
    @Transactional
    public void deleteUserById(long userId) {
        getUserById(userId);
        userRepository.deleteById(userId);
    }

    private void checkUserEmailForDuplicate(String email) {
        if (userRepository.existsByEmail(email) && !email.isBlank()) {
            throw new DuplicateEmailException(String.format("Пользователь с почтой {} уже сущетвует", email));
        }
    }
}

