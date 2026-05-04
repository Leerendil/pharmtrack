package org.vsu.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank(message = "username is required")
    @Size(min = 3, max = 10, message = "username must be between 3 and 10 characters")
    private String username;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 10, message = "First name must be between 2 and 10 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 10, message = "Last name must be between 2 and 10 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Keycloak Id is required")
    private UUID keycloakId;
}
