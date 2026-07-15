package com.project.user_service;

import com.project.user_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserDataLoaderTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void debeCargarUsuariosFakeAlIniciar() {
        long totalUsuarios = userRepository.count();
        assertTrue(totalUsuarios > 0);
    }
}