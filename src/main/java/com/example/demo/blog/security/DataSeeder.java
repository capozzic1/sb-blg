package com.example.demo.blog.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Objects;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String adminUsername;
    private final String adminPassword;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder,
                      @Value("${admin.username}") String adminUsername,
                      @Value("${admin.password}") String adminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        if (Objects.isNull(adminUsername) || adminUsername.isBlank()) {
            throw new IllegalStateException("Required property 'admin.username' is missing or empty. Set it in application.properties or as an environment variable (ADMIN_USERNAME).");
        }
        if (Objects.isNull(adminPassword) || adminPassword.isBlank()) {
            throw new IllegalStateException("Required property 'admin.password' is missing or empty. Set it in application.properties or as an environment variable (ADMIN_PASSWORD).");
        }
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Optional<User> opt = userRepository.findByUsername(adminUsername);
        if (opt.isEmpty()) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.addRole("ROLE_ADMIN");
            userRepository.save(admin);
            System.out.println("Created admin user: " + adminUsername);
        } else {
            User existing = opt.get();
            if (!existing.getRoles().contains("ROLE_ADMIN")) {
                existing.addRole("ROLE_ADMIN");
                userRepository.save(existing);
                System.out.println("Upgraded existing user to admin: " + adminUsername);
            } else {
                System.out.println("Admin user already exists: " + adminUsername);
            }
        }
    }
}
