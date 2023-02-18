package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemByRequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@Builder
public class ItemRequestOutput {

    private long id;

    private String description;
    private Requester requester;
    private LocalDateTime created;
    private List<ItemByRequestDto> items;

    public ItemRequestOutput(long id, String description, Requester requester,
                             LocalDateTime created, List<ItemByRequestDto> items) {
        this.id = id;
        this.description = description;
        this.requester = requester;
        this.created = created;
        this.items = items;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Requester {
        private long id;
        private String name;

        public Requester(User requester) {
            this.id = requester.getId();
            this.name = requester.getName();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Requester requester = (Requester) o;
            return id == requester.id && Objects.equals(name, requester.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }
    }
}
