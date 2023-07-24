package ru.practicum.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Event;
import ru.practicum.request.enums.Status;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime created;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    private User requester;

    @Enumerated(EnumType.STRING)
    private Status status;
}
