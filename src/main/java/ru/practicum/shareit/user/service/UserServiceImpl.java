package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        try {
            User user = UserMapper.toUser(userDto);
            return UserMapper.toUserDto(userRepository.save(user));
        } catch (Exception e) {
            throw new ValidateException("Пользователь с почтой " + userDto.getEmail() + " уже существует");
        }
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, long userId) {
        User userToUpdate = UserMapper.toUser(userDto);
        checkUserEmailForDuplicate(userToUpdate);
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        } else {
            userToUpdate.setId(userId);
            if (userToUpdate.getName() == null) {
                userToUpdate.setName(user.get().getName());
            }
            if (userToUpdate.getEmail() == null) {
                userToUpdate.setEmail(user.get().getEmail());
            }
            return UserMapper.toUserDto(userRepository.save(userToUpdate));
        }
    }

    @Override
    @Transactional
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto getUserById(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        return UserMapper.toUserDto(user.get());
    }


    @Override
    @Transactional
    public void deleteUserById(long userId) {
        getUserById(userId);
        userRepository.deleteById(userId);
    }

    public void checkUserEmailForDuplicate(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ValidateException("Пользователь с почтой " + user.getEmail() + " уже существует");
        }
    }
}

