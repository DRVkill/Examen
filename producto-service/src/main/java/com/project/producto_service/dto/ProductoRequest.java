package com.project.producto_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductoRequest(
        @NotBlank String titulo,
        @Positive Double precio,
        @PositiveOrZero Integer duracionHoras,
        Integer categoriaId
) {}