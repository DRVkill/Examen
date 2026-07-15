package com.project.order_service.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Integer id,
        Integer userId,
        Double total,
        String estado,
        LocalDateTime fechaCreacion,
        List<ItemOrderResponse> items
) {

    public record ItemOrderResponse(
            Integer productoId,
            String productoTitulo,
            Integer cantidad,
            Double precioUnitario,
            Double subtotal
    ) {}
}