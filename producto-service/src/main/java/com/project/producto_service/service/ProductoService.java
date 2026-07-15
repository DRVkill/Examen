package com.project.producto_service.service;

import com.project.producto_service.dto.ProductoRequest;
import com.project.producto_service.dto.ProductoResponse;
import com.project.producto_service.exception.ResourceNotFoundException;
import com.project.producto_service.model.Producto;
import com.project.producto_service.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<ProductoResponse> listarTodos() {
        return productoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProductoResponse obtenerPorId(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        return mapToResponse(producto);
    }

    public ProductoResponse crear(ProductoRequest request) {
        productoRepository.findByTitulo(request.titulo())
                .ifPresent(p -> {
                    throw new IllegalArgumentException("Ya existe un producto con ese titulo");
                });

        Producto producto = new Producto();
        producto.setTitulo(request.titulo());
        producto.setPrecio(request.precio());
        producto.setDuracionHoras(request.duracionHoras());
        producto.setCategoriaId(request.categoriaId());
        producto.setActivo(true);

        Producto guardado = productoRepository.save(producto);
        return mapToResponse(guardado);
    }

    public ProductoResponse actualizar(Integer id, ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        productoRepository.findByTitulo(request.titulo())
                .ifPresent(p -> {
                    if (!p.getId().equals(id)) {
                        throw new IllegalArgumentException("Ya existe un producto con ese titulo");
                    }
                });

        producto.setTitulo(request.titulo());
        producto.setPrecio(request.precio());
        producto.setDuracionHoras(request.duracionHoras());
        producto.setCategoriaId(request.categoriaId());

        Producto actualizado = productoRepository.save(producto);
        return mapToResponse(actualizado);
    }

    public void eliminar(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        productoRepository.delete(producto);
    }

    private ProductoResponse mapToResponse(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getTitulo(),
                producto.getPrecio(),
                producto.getDuracionHoras(),
                producto.getCategoriaId(),
                producto.getActivo()
        );
    }
}