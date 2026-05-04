package org.vsu.authservice.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
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

    @NotBlank(message = "Password is required")
    @Size(min = 5, max = 15, message = "Password must be between 5 and 15 characters")
    private String rawPassword;
}
