package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemByRequestDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestOutput {

    private long id;
    @NotBlank
    private String description;
    private User requester;
    private LocalDateTime created;
    private List<ItemByRequestDto> items;
}