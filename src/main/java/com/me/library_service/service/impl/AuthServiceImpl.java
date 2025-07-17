package com.me.library_service.service.impl;

import com.me.library_service.exception.UserException;
import com.me.library_service.model.request.AuthRequest;
import com.me.library_service.model.response.AuthResponse;
import com.me.library_service.persistence.entity.Role;
import com.me.library_service.persistence.entity.User;
import com.me.library_service.persistence.repository.RoleRepository;
import com.me.library_service.persistence.repository.UserRepository;
import com.me.library_service.service.AuthService;
import com.me.library_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${app.defaultUserRole}")
    private String defaultUserRole;

    @Override
    @Transactional
    public AuthResponse register(AuthRequest authRequest) {

        if (userRepository.existsByEmail(authRequest.getEmail())) {
            throw new UserException("Email already exists");
        }

        Role readerRole = roleRepository.findByName(defaultUserRole)
                .orElseGet(() -> roleRepository.save(Role.builder().name(defaultUserRole).build()));

        User user = User.builder()
                .email(authRequest.getEmail())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .roles(Set.of(readerRole))
                .build();
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        Set<String> roleNames = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

        return new AuthResponse(token, roleNames);
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new UserException("Invalid credentials"));

        boolean matches = passwordEncoder.matches(authRequest.getPassword(), user.getPassword());

        if(!matches){
            throw new UserException("Invalid Credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        Set<String> roleNames = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        return new AuthResponse(token, roleNames);
    }
}
