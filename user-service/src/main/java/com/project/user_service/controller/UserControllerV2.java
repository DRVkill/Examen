package com.project.user_service.controller;

import com.project.user_service.assemblers.UserModelAssembler;
import com.project.user_service.dto.UserRequest;
import com.project.user_service.dto.UserResponse;
import com.project.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/usuarios")
public class UserControllerV2 {

    private final UserService userService;
    private final UserModelAssembler assembler;

    public UserControllerV2(UserService userService, UserModelAssembler assembler) {
        this.userService = userService;
        this.assembler = assembler;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<UserResponse>> listarTodos() {
        List<EntityModel<UserResponse>> usuarios = userService.listarTodos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(usuarios,
                linkTo(methodOn(UserControllerV2.class).listarTodos()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<UserResponse> obtenerPorId(@PathVariable Integer id) {
        UserResponse user = userService.obtenerPorId(id);
        return assembler.toModel(user);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<UserResponse>> crear(@Valid @RequestBody UserRequest request) {
        UserResponse nuevo = userService.crear(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(nuevo));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<UserResponse>> actualizar(@PathVariable Integer id,
                                                                @Valid @RequestBody UserRequest request) {
        UserResponse actualizado = userService.actualizar(id, request);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        userService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}