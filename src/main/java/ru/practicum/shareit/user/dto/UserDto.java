package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDto {
    private long id;
    @NotBlank(groups = {Create.class})
    @Size(max = 255)
    private String name;
    @NotBlank (groups = {Create.class})
    @Email(groups = {Update.class, Create.class})
    @Size(max = 512)
    private String email;
}
