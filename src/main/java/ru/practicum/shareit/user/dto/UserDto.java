package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDto {
    private long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @NotBlank (groups = {Create.class})
    @Email(groups = {Update.class, Create.class})
    private String email;
}
