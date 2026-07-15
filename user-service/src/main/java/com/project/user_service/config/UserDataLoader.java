package com.project.user_service.config;

import com.project.user_service.model.User;
import com.project.user_service.repository.UserRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    public UserDataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            Faker faker = new Faker();

            for (int i = 0; i < 15; i++) {
                User user = new User();
                user.setNombre(faker.name().fullName());
                user.setEmail(faker.internet().emailAddress());
                user.setRol("Cliente");

                userRepository.save(user);
            }
        }
    }
}