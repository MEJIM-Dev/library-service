package com.me.library_service.service.impl;

import com.me.library_service.exception.CustomException;
import com.me.library_service.model.request.AuthRequest;
import com.me.library_service.model.response.AuthResponse;
import com.me.library_service.persistence.entity.Role;
import com.me.library_service.persistence.entity.User;
import com.me.library_service.persistence.entity.UserRole;
import com.me.library_service.persistence.repository.RoleRepository;
import com.me.library_service.persistence.repository.UserRepository;
import com.me.library_service.persistence.repository.UserRoleRepository;
import com.me.library_service.service.AuthService;
import com.me.library_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${app.defaultUserRole}")
    private String defaultUserRole;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public AuthResponse register(AuthRequest authRequest) {

        if (userRepository.existsByEmail(authRequest.getEmail())) {
            throw new CustomException("Email already exists");
        }

        Role readerRole = roleRepository.findByName(defaultUserRole)
                .orElseGet(() -> roleRepository.save(Role.builder().name(defaultUserRole).build()));

        User user = User.builder()
                .email(authRequest.getEmail())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .build();
        user = userRepository.save(user);

        UserRole userRole = UserRole.builder()
                .role(readerRole)
                .user(user)
                .build();

        userRoleRepository.save(userRole);

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new CustomException("Invalid credentials"));

        boolean matches = passwordEncoder.matches(authRequest.getPassword(), user.getPassword());

        if(!matches){
            throw new CustomException("Invalid Credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token);
    }
}
