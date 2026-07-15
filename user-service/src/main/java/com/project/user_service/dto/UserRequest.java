package com.project.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @Email
        @NotBlank
        String email,

        @NotBlank
        @Size(max = 80)
        String nombre,

        String rol
) {}