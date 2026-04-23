package org.vsu.userservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vsu.userservice.dto.UserDto;
import org.vsu.userservice.dto.UserResponse;
import org.vsu.userservice.entity.User;
import org.vsu.userservice.mapper.UserMapper;
import org.vsu.userservice.repository.UserRepository;
import org.vsu.userservice.utils.exceptions.UserAlreadyExistsException;
import org.vsu.userservice.utils.exceptions.UserNotFoundException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public final UserMapper userMapper;

    public UserResponse create(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException(userDto.getEmail());
        }

        User entity = userMapper.mapToEntity(userDto);

        entity = userRepository.save(entity);

        return userMapper.mapToResponse(entity);
    }

    public UserResponse findByEmail(String email) {
        User entity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Not found user by email"));

        return userMapper.mapToResponse(entity);
    }
}
