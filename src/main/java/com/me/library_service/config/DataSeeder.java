package com.me.library_service.config;

import com.me.library_service.persistence.entity.Role;
import com.me.library_service.persistence.entity.User;
import com.me.library_service.persistence.repository.RoleRepository;
import com.me.library_service.persistence.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeeder {

    @Value("${dataSeeder.active:true}")
    private boolean dataSeederActive;

    @Value("${app.defaultAdminRole:LIBRARIAN}")
    private String adminRole;

    @Value("${app.defaultAdminUsername:admin@library.com}")
    private String adminEmail;

    @Value("${app.defaultAdminPassword:admin123}")
    private String adminPassword;

    @Value("${app.defaultRoles}")
    private String[] defaultRoles;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void seed() {
        if (!dataSeederActive) {
            return;
        }

        Arrays.stream(defaultRoles).forEach(defaultRole-> {
            if(!roleRepository.existsByNameIgnoreCase(defaultRole)){
                Role role = Role.builder()
                        .name(defaultRole)
                        .build();

                roleRepository.save(role);
            }

        });

        Role librarianRole = roleRepository.findByName(adminRole)
                .orElseGet(() -> roleRepository.save(Role.builder().name(adminRole).build()));

        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .roles(Set.of(librarianRole))
                    .build();
            userRepository.save(admin);

        }
    }

}
