package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotAllowedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
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
            throw new DuplicateEmailException("Пользователь с почтой " + userDto.getEmail() + " уже существует");
        }
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, long userId) {
        User userToUpdate = UserMapper.toUser(userDto);
        checkUserEmailForDuplicate(userToUpdate);
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            if (user.get().getId() == userId) {
                userToUpdate.setId(userId);
                if (userToUpdate.getName() == null) {
                    userToUpdate.setName(user.get().getName());
                }
                if (userToUpdate.getEmail() == null) {
                    userToUpdate.setEmail(user.get().getEmail());
                }
                return UserMapper.toUserDto(userRepository.save(userToUpdate));
            } else {
                throw new NotAllowedException("Пользователь с id = " + user.get().getId() +
                        " не может изменить пользователя с id = " + userId);
            }
        } else {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
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
            throw new DuplicateEmailException("Пользователь с почтой " + user.getEmail() + " уже существует");
        }
    }
}

