package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

public class UserMapperTest {

    @Test
    public void mapperToUserTest() {
        var userDto = UserDto.builder().id(1).name("Name").email("qwer@mail.ru").build();

        User result = UserMapper.toUser(userDto);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Name", result.getName());
        Assertions.assertEquals("qwer@mail.ru", result.getEmail());
    }

    @Test
    public void mapperToUserDtoTest() {
        var user = new User(1, "Name", "qwer@mail.ru");

        UserDto result = UserMapper.toUserDto(user);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Name", result.getName());
        Assertions.assertEquals("qwer@mail.ru", result.getEmail());
    }
}
