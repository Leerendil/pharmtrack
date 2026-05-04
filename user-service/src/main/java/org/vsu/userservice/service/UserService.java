package org.vsu.userservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.vsu.userservice.dto.UserDto;
import org.vsu.userservice.dto.UserResponse;
import org.vsu.userservice.entity.Roles;
import org.vsu.userservice.entity.User;
import org.vsu.userservice.mapper.UserMapper;
import org.vsu.userservice.repository.UserRepository;
import org.vsu.userservice.utils.exceptions.UserAlreadyExistsException;
import org.vsu.userservice.utils.exceptions.UserNotFoundException;

import java.util.UUID;

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

        entity.setRole(Roles.USER);
        entity.setDeactivated(false);
        entity = userRepository.save(entity);

        return userMapper.mapToResponse(entity);
    }

    public String deactivate(Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());

        User entity = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("userId: "+userId));

        entity.setDeactivated(true);

        userRepository.save(entity);

        return "Your account was successfully deactivated!";
    }

    public UserResponse findByEmail(String email) {
        User entity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Not found user by email"));

        if (entity.isDeactivated()) {
            //TODO: Заменить на свой UserIsDeactivatedException()
            throw new RuntimeException("409; UserIsDeactivatedException()");
        }

        return userMapper.mapToResponse(entity);
    }
}
