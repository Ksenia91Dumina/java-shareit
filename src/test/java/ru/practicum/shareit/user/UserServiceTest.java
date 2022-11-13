package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotAllowedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository repository;

    private final User user = new User(1, "Name", "qwer@mail.ru");


    private UserDto getUserDto() {
        return UserDto.builder()
                .name("Name_2")
                .email("zxcv@mail.ru")
                .build();
    }

    @BeforeEach
    void beforeEach() {
        repository = mock(UserRepository.class);
        userService = new UserServiceImpl();
        userService.userRepository = repository;
        userService.createUser(UserMapper.toUserDto(user));
    }

    @AfterEach
    void afterEach() {
        repository.deleteAll();
    }


    @Test
    public void getUserByIdTest() {
        when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Name", result.getName());
        Assertions.assertEquals("qwer@mail.ru", result.getEmail());
    }

    @Test
    public void createTest() {
        when(repository.save(Mockito.any(User.class)))
                .thenReturn(new User(3L, "Name_3", "asdf@mail.ru"));

        UserDto result = userService.createUser(getUserDto());

        Assertions.assertEquals(3, result.getId());
        Assertions.assertEquals("Name_3", result.getName());
        Assertions.assertEquals("asdf@mail.ru", result.getEmail());
    }

    @Test
    public void createUserWithDuplicateEmailTest() {
        when(repository.save(Mockito.any(User.class)))
                .thenThrow(new DuplicateEmailException("Пользователь с почтой zxcv@mail.ru уже существует"));

        final DuplicateEmailException exception = Assertions.assertThrows(
                DuplicateEmailException.class,
                () -> userService.createUser(getUserDto()));

        Assertions.assertEquals("Пользователь с почтой zxcv@mail.ru уже существует", exception.getMessage());
    }

    @Test
    public void getAllUsersTest() {
        final PageImpl<User> userPage = new PageImpl<>(Collections.singletonList(user));
        when(repository.findAll(PageRequest.ofSize(10)))
                .thenReturn(userPage);

        List<UserDto> result = userService.getAllUsers(PageRequest.ofSize(10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals("Name", result.get(0).getName());
        Assertions.assertEquals("qwer@mail.ru", result.get(0).getEmail());
    }

    @Test
    public void getNotExistsUserByIdTest() {
        when(repository.findById(Mockito.anyLong()))
                .thenThrow(new NotFoundException("Пользователь с id = 1 не найден"));

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.getUserById(1));

        Assertions.assertEquals("Пользователь с id = 1 не найден", exception.getMessage());
    }

    @Test
    public void updateIllegalUserTest() {
        when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        final NotAllowedException exception = Assertions.assertThrows(
                NotAllowedException.class,
                () -> userService.updateUser(getUserDto(), 2L));

        Assertions.assertEquals("Пользователь с id = 1 не может изменить пользователя с id = 2",
                exception.getMessage());
    }

    @Test
    public void updateNotExistsUser() {
        when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.updateUser(getUserDto(), 3L));

        Assertions.assertEquals("Пользователь с id = 3 не найден", exception.getMessage());
    }

    @Test
    public void updateEmail() {
        when(repository.save(Mockito.any()))
                .thenReturn(new User(1L, "Name", "new@mail.com"));

        var result = userService.updateUser(UserDto.builder().email("new@mail.com").build(), 1L);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Name", result.getName());
        Assertions.assertEquals("new@mail.com", result.getEmail());
    }

    @Test
    public void updateName() {
        when(repository.save(Mockito.any()))
                .thenReturn(new User(1L, "new_name", "qwer@mail.ru"));

        var result = userService.updateUser(UserDto.builder().name("new_name").build(), 1L);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("new_name", result.getName());
        Assertions.assertEquals("qwer@mail.ru", result.getEmail());
    }
}
