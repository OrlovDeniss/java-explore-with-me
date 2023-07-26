package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.pagerequest.PageRequester;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsersByAdmin(Set<Integer> ids, Integer from, Integer size) {
        return toDto(userRepository.findAllByIdIn(ids, new PageRequester(from, size, Sort.by("id"))).toList());
    }

    @Override
    public UserDto saveNewUserByAdmin(NewUserRequest newUserRequest) {
        return toDto(userRepository.save(toEntity(newUserRequest)));
    }

    @Override
    public void deleteUserByAdmin(Integer userId) {
        userRepository.deleteById(userId);
    }

    private User toEntity(NewUserRequest newUserRequest) {
        return UserMapper.INSTANCE.toEntity(newUserRequest);
    }

    private UserDto toDto(User user) {
        return UserMapper.INSTANCE.toDto(user);
    }

    private List<UserDto> toDto(List<User> user) {
        return UserMapper.INSTANCE.toDto(user);
    }
}
