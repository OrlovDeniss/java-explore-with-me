package ru.practicum.event.model;

import lombok.Data;
import ru.practicum.category.model.Category;
import ru.practicum.event.enums.State;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int participantLimit;
    private int confirmedRequests;

    private String title;
    private String annotation;
    private String description;

    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    private LocalDateTime eventDate;

    private boolean requestModeration;
    private boolean paid;

    @Enumerated(EnumType.STRING)
    private State state;

    @Embedded
    private Location location;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User initiator;

    @Transient
    private long views;
}
