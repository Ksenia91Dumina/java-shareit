package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items", schema = "public")
public class Item {

    @Id
    private long id;
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    @Column(name = "description", nullable = false, length = 4000)
    private String description;
    @Column(name = "available", nullable = false)
    private Boolean available;
    @Column(name = "owner_id", nullable = false)
    private long ownerId;
    @Column(name = "request_id")
    private Long requestId;
}
