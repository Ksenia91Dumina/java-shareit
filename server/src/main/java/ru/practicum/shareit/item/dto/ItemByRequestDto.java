package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.request.repository.ItemRequestRepository;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ItemByRequestDto {

    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long ownerId;
    private long requestId;

    private ItemRequestRepository requestRepository;

    public ItemByRequestDto(long id, String name, String description,
                            Boolean available, long ownerId, long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.ownerId = ownerId;
        this.requestId = requestId;
    }

}