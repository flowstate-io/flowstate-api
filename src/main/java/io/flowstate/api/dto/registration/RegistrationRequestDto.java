package io.flowstate.api.dto.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequestDto(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
        String password,

        @NotBlank(message = "First Name is required")
        @Size(min = 3, max = 20, message = "First name must be between 3 and 20 characters")
        String firstName,

        @NotBlank(message = "Last Name is required")
        @Size(min = 3, max = 20, message = "Last name must be between 3 and 20 characters")
        String lastName
) {
}
