package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder
public class ItemRequestDto {

    private long id;
    @NotBlank(groups = {Create.class})
    @Size(max = 4000)
    private String description;
    private Requester requester;
    private LocalDateTime created;

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
