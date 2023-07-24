package ru.practicum.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.user.model.User;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM users u " +
            "WHERE ((:ids) IS NULL OR u.id IN (:ids)) ")
    Page<User> findAllByIdIn(Set<Integer> ids, Pageable pageable);

}
