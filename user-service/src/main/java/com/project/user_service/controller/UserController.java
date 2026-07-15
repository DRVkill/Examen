package com.project.user_service.controller;

import com.project.user_service.dto.UserRequest;
import com.project.user_service.dto.UserResponse;
import com.project.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operaciones CRUD para el microservicio de usuarios")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Listar todos los usuarios")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserResponse>>> listarTodos() {
        List<EntityModel<UserResponse>> usuarios = userService.listarTodos()
                .stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).obtenerPorId(user.id())).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<UserResponse>> collectionModel = CollectionModel.of(
                usuarios,
                linkTo(methodOn(UserController.class).listarTodos()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Obtener un usuario por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponse>> obtenerPorId(@PathVariable Integer id) {
        UserResponse user = userService.obtenerPorId(id);

        EntityModel<UserResponse> model = EntityModel.of(user,
                linkTo(methodOn(UserController.class).obtenerPorId(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).listarTodos()).withRel("usuarios"),
                linkTo(methodOn(UserController.class).eliminar(id)).withRel("eliminar"),
                linkTo(methodOn(UserController.class).actualizar(id, null)).withRel("actualizar")
        );

        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Crear un nuevo usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<UserResponse>> crear(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.crear(request);

        EntityModel<UserResponse> model = EntityModel.of(response,
                linkTo(methodOn(UserController.class).obtenerPorId(response.id())).withSelfRel(),
                linkTo(methodOn(UserController.class).listarTodos()).withRel("usuarios")
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @Operation(summary = "Actualizar un usuario existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponse>> actualizar(@PathVariable Integer id,
                                                                @Valid @RequestBody UserRequest request) {
        UserResponse response = userService.actualizar(id, request);

        EntityModel<UserResponse> model = EntityModel.of(response,
                linkTo(methodOn(UserController.class).obtenerPorId(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).listarTodos()).withRel("usuarios")
        );

        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Eliminar un usuario por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        userService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}