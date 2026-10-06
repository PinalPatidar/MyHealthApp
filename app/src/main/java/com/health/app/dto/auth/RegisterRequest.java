package com.health.app.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min=12, max=128, message = "Password must be between 12 and 128 characters")
        String password,

        @NotBlank(message = "Confirm password is required")
        String confirmPassword
) {
}
