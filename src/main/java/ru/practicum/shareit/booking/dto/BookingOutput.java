package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder
public class BookingOutput {

    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private Booker booker;
    private BookingStatus status;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Booker {
        private long id;
        private String name;

        public Booker(User booker) {
            this.id = booker.getId();
            this.name = booker.getName();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Booker booker = (Booker) o;
            return id == booker.id && Objects.equals(name, booker.name);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private long id;
        private String name;

        public Item(ru.practicum.shareit.item.model.Item mainItem) {
            this.id = mainItem.getId();
            this.name = mainItem.getName();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            return id == item.id && Objects.equals(name, item.name);
        }

    }
}