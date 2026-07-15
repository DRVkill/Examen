package com.project.user_service.dto;

public record UserResponse(
        Integer id,
        String email,
        String nombre,
        String rol
) {}