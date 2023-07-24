package ru.practicum.user.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;

public interface UserAdminController {

    String FROM = "0";
    String SIZE = "10";

    @GetMapping
    ResponseEntity<List<UserDto>> getUsersByAdmin(
            @RequestParam(required = false) Set<Integer> ids,
            @RequestParam(required = false, defaultValue = FROM) @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = SIZE) @Positive Integer size);

    @PostMapping
    ResponseEntity<UserDto> saveNewUserByAdmin(@Valid @RequestBody NewUserRequest newUserRequest);

    @DeleteMapping("{userId}")
    ResponseEntity<Object> deleteUserByAdmin(@PathVariable @Positive Integer userId);

}