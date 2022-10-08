package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EmailValidateException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("UserRepository")
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> allUsers = new HashMap<>();
    private long id = 0L;

    @Override
    public User createUser(User user) {
        user.setId(++id);
        checkUserEmailForDuplicate(user);
        allUsers.put(user.getId(), user);
        return user;
    }

    public void checkUserEmailForDuplicate(User user) {
        for (User userToCheck : getAllUsers()) {
            if (userToCheck.getEmail().equals(user.getEmail())) {
                throw new EmailValidateException("Пользователь с почтой " + user.getEmail() + " уже существует");
            }
        }
    }

    @Override
    public User updateUser(User user) {
        for (User userToCheck : getAllUsers()) {
            if (userToCheck.getId() == user.getId()) {
                if (user.getName() != null) {
                    userToCheck.setName(user.getName());
                }
                if (user.getEmail() != null) {
                    userToCheck.setEmail(user.getEmail());
                }
            }
        }
        return getUserById(user.getId());
    }


    @Override
    public List<User> getAllUsers() {
        return List.copyOf(allUsers.values());
    }

    @Override
    public User getUserById(long userId) {
        return getAllUsers().stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void deleteUserById(long userId) {
        if (!allUsers.isEmpty()) {
            allUsers.remove(userId);
        }
    }
}

