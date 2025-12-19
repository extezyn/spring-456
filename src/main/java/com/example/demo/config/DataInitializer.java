package com.example.demo.config;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("ADMIN").build()));
            Role teacherRole = roleRepository.findByName("TEACHER")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("TEACHER").build()));
            Role studentRole = roleRepository.findByName("STUDENT")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("STUDENT").build()));

            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("admin123"))
                        .enabled(true)
                        .role(adminRole)
                        .build();
                userRepository.save(admin);
            }
        };
    }
}


