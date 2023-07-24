package ru.practicum.user.service;

import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<UserDto> getUsersByAdmin(Set<Integer> ids, Integer from, Integer size);

    UserDto saveNewUserByAdmin(NewUserRequest newUserRequest);

    void deleteUserByAdmin(Integer userId);

}
