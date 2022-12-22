package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.additions.Create;
import ru.practicum.shareit.additions.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
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

    public UserDto(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
