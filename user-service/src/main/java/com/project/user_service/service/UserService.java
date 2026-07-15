package com.project.user_service.service;

import com.project.user_service.dto.UserRequest;
import com.project.user_service.dto.UserResponse;
import com.project.user_service.exception.ResourceNotFoundException;
import com.project.user_service.model.User;
import com.project.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> listarTodos() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public UserResponse obtenerPorId(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User no encontrado con id: " + id)
                );
        return mapToResponse(user);
    }

    public UserResponse crear(UserRequest request) {
        userRepository.findByEmail(request.email())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Ya existe un user con ese email");
                });

        User user = new User();
        user.setEmail(request.email());
        user.setNombre(request.nombre());
        user.setRol(request.rol() != null ? request.rol() : "Cliente");

        User guardado = userRepository.save(user);
        return mapToResponse(guardado);
    }

    public UserResponse actualizar(Integer id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User no encontrado con id: " + id)
                );

        userRepository.findByEmail(request.email())
                .ifPresent(u -> {
                    if (!u.getId().equals(id)) {
                        throw new IllegalArgumentException("Ya existe un user con ese email");
                    }
                });

        user.setEmail(request.email());
        user.setNombre(request.nombre());
        if (request.rol() != null) {
            user.setRol(request.rol());
        }

        User actualizado = userRepository.save(user);
        return mapToResponse(actualizado);
    }

    public void eliminar(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User no encontrado con id: " + id)
                );
        userRepository.delete(user);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNombre(),
                user.getRol()
        );
    }
}