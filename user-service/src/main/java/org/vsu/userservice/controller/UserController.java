package org.vsu.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.vsu.userservice.dto.UserDto;
import org.vsu.userservice.dto.UserResponse;
import org.vsu.userservice.service.UserService;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserAPI {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(userDto));
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> findByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<String> deactivate(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(userService.deactivate(jwt));
    }
}
