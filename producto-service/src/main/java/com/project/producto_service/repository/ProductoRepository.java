package com.project.producto_service.repository;

import com.project.producto_service.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    Optional<Producto> findByTitulo(String titulo);
    List<Producto> findByCategoriaIdAndActivoTrue(Integer categoriaId);
    List<Producto> findByTituloContainingIgnoreCase(String titulo);
}