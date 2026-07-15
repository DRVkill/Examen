package com.project.producto_service.dto;

public record ProductoResponse(
        Integer id,
        String titulo,
        Double precio,
        Integer duracionHoras,
        Integer categoriaId,
        Boolean activo
) {}