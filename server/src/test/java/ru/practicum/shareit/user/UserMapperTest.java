package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.List;

public class UserMapperTest {

    User user = new User(1, "Name", "qwer@mail.ru");
    User user2 = new User(2, "Name_2", "asdf@gmail.com");

    @Test
    public void mapToUserTest() {
        var userDto = UserDto.builder().id(1).name("Name").email("qwer@mail.ru").build();

        User result = UserMapper.toUser(userDto);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Name", result.getName());
        Assertions.assertEquals("qwer@mail.ru", result.getEmail());
    }

    @Test
    public void mapToUserDtoTest() {
        UserDto result = UserMapper.toUserDto(user);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Name", result.getName());
        Assertions.assertEquals("qwer@mail.ru", result.getEmail());
    }

    @Test
    public void mapAllUsersToDto() {
        List<UserDto> result = UserMapper.getAllUsersToDto(List.of(user, user2));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Name", result.get(0).getName());
        Assertions.assertEquals("Name_2", result.get(1).getName());
    }

}
