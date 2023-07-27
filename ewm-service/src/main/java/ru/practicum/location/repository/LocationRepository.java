package ru.practicum.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.location.model.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    boolean existsByLatAndLon(Double lat, Double lon);

}
