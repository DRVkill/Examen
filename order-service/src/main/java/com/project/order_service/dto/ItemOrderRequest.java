package com.project.order_service.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;



public record ItemOrderRequest(
        @NotNull Integer productoId,
        @Positive Integer cantidad
) {}
