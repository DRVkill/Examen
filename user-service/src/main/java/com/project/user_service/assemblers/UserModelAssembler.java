package com.project.user_service.assemblers;

import com.project.user_service.controller.UserControllerV2;
import com.project.user_service.dto.UserResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserResponse, EntityModel<UserResponse>> {

    @Override
    public EntityModel<UserResponse> toModel(UserResponse user) {
        return EntityModel.of(user,
                linkTo(methodOn(UserControllerV2.class).obtenerPorId(user.id())).withSelfRel(),
                linkTo(methodOn(UserControllerV2.class).listarTodos()).withRel("usuarios"));
    }
}