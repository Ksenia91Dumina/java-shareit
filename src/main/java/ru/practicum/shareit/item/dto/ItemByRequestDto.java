package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemByRequestDto {

    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long ownerId;
    private long requestId;

    public ItemByRequestDto(long id, String name, String description,
                            Boolean available, long ownerId, long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.ownerId = ownerId;
        this.requestId = requestId;
    }

  /*  public long getRequestId() {
        return request.getId();
    }

    public void setRequestId(long id) {
        this.request.setId(id);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private long id;
        @NotBlank(groups = {Create.class})
        @Size(max = 4000, groups = {Create.class})
        private String description;

        public Request(ItemRequest request) {
            this.id = request.getId();
            this.description = request.getDescription();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Request request = (Request) o;
            return id == request.id && Objects.equals(description, request.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, description);
        }
    }*/
}
