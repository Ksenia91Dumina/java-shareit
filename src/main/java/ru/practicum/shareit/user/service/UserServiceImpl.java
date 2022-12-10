package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.MyPageRequest;
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

    @Autowired
    public UserRepository userRepository;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        try {
            User user = UserMapper.toUser(userDto);
            return UserMapper.toUserDto(userRepository.save(user));
        } catch (Exception e) {
            throw new DuplicateEmailException("Пользователь с почтой " + userDto.getEmail() + " уже существует");
        }
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, long userId) {
        checkUserEmailForDuplicate(userDto.getEmail());
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + userId + " не найден"));
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
            throw new NotAllowedException("Пользователь с id = " + user.getId() +
                    " не может изменить пользователя с id = " + userId);
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
                new NotFoundException("Пользователь с id = " + userId + " не найден"));
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
            throw new DuplicateEmailException("Пользователь с почтой " + email + " уже сущетвует");
        }
    }
}

