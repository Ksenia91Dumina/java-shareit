package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequestDto {

    private long id;
    @NotBlank(groups = {Create.class})
    @Size(max = 4000)
    private String description;
    private User requester;
    private LocalDateTime created;
}
