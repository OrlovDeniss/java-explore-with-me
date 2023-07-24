package ru.practicum.user.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import java.util.List;
import java.util.Set;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserAdminControllerImpl implements UserAdminController {
    private final UserService userService;

    @Override
    public ResponseEntity<List<UserDto>> getUsersByAdmin(Set<Integer> ids, Integer from, Integer size) {
        log.info("GET /admin/users ids: {}, from: {}. size: {}.", ids, from, size);
        return new ResponseEntity<>(userService.getUsersByAdmin(ids, from, size), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDto> saveNewUserByAdmin(NewUserRequest newUserRequest) {
        log.info("POST /admin/users dto: {}.", newUserRequest);
        return new ResponseEntity<>(userService.saveNewUserByAdmin(newUserRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> deleteUserByAdmin(Integer userId) {
        log.info("DELETE /admin/users/{}", userId);
        userService.deleteUserByAdmin(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
