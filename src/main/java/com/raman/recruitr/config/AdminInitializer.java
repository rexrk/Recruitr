package com.raman.recruitr.config;

import com.raman.recruitr.entity.AppUser;
import com.raman.recruitr.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer {

    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Bean
    CommandLineRunner initAdmin(AppUserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                AppUser admin = AppUser.builder()
                        .username(adminUsername)
                        .password(passwordEncoder.encode(adminPassword))
                        .role(AppUser.Role.ADMIN)
                        .build();

                userRepository.save(admin);
                log.info("✅ Admin created with Credentials {} : {}", adminUsername, adminPassword);
            }
        };
    }
}
