package io.flowstate.api.dto.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailVerificationRequestDto(
        @NotBlank(message = "Email is required")
        @Email
        String email,

        @NotBlank(message = "OTP is required")
        String otp
) {
}
