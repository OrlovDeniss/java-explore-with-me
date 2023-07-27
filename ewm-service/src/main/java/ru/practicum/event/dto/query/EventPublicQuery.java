package ru.practicum.event.dto.query;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.event.enums.Sort;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class EventPublicQuery {
    private String text;
    private Set<Integer> categories;
    private Set<Integer> locations;
    private Boolean paid;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeStart = LocalDateTime.now();

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeEnd = LocalDateTime.of(9999, 12, 31, 23, 59, 59);

    private boolean onlyAvailable;
    private Sort sort = Sort.EVENT_DATE;
}
