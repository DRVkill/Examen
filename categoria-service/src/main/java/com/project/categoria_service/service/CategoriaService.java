package com.project.categoria_service.service;

import com.project.categoria_service.dto.CategoriaRequest;
import com.project.categoria_service.dto.CategoriaResponse;
import com.project.categoria_service.exception.ResourceNotFoundException;
import com.project.categoria_service.model.Categoria;
import com.project.categoria_service.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaResponse> listarTodas() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CategoriaResponse obtenerPorId(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));

        return mapToResponse(categoria);
    }

    public CategoriaResponse crear(CategoriaRequest request) {
        categoriaRepository.findByNombre(request.getNombre())
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Ya existe una categoria con ese nombre");
                });

        Categoria categoria = new Categoria();
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());

        Categoria guardada = categoriaRepository.save(categoria);
        return mapToResponse(guardada);
    }

    public CategoriaResponse actualizar(Integer id, CategoriaRequest request) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));

        categoriaRepository.findByNombre(request.getNombre())
                .ifPresent(c -> {
                    if (!c.getId().equals(id)) {
                        throw new IllegalArgumentException("Ya existe una categoria con ese nombre");
                    }
                });

        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());

        Categoria actualizada = categoriaRepository.save(categoria);
        return mapToResponse(actualizada);
    }

    public void eliminar(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));

        categoriaRepository.delete(categoria);
    }

    private CategoriaResponse mapToResponse(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );
    }
}
