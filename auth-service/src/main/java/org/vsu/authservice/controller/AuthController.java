package org.vsu.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vsu.authservice.dto.AssignRoleDto;
import org.vsu.authservice.dto.LoginDto;
import org.vsu.authservice.dto.RegisterDto;
import org.vsu.authservice.dto.UserResponse;
import org.vsu.authservice.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthAPI{
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid RegisterDto registerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody @Valid LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/roles/assign")
    public ResponseEntity<String> assignRoleToUser(@RequestBody @Valid AssignRoleDto roleDto) {
        return ResponseEntity.ok(authService.assignRoleToUser(roleDto.getKeycloakId(), roleDto.getRoleName()));
    }
}
