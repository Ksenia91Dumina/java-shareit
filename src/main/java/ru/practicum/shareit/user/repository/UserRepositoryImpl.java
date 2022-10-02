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
        for (User userToCheck : getAllUsers()) {
            if (user.getEmail().equals(userToCheck.getEmail()))  {
                throw new EmailValidateException("Пользователь с почтой " + user.getEmail() + " уже существует");
            }
        }
        allUsers.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        User userToCheck = allUsers.get(user.getId());
        if (user.getName() != null) {
            userToCheck.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (user.getEmail().equals(userToCheck.getEmail())) {
                throw new EmailValidateException("Пользователь с почтой " + user.getEmail() + " уже существует");
            } else {
                userToCheck.setEmail(user.getEmail());
            }
        }
        return userToCheck;
    }


    @Override
    public List<User> getAllUsers() {
        return List.copyOf(allUsers.values());
    }

    @Override
    public User getUserById(long userId) {
        return allUsers.get(userId);
    }

    @Override
    public void deleteUserById(long userId) {
        if (!allUsers.isEmpty()) {
            allUsers.remove(userId);
        }
    }
}

