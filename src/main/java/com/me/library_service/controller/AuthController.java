package com.me.library_service.controller;

import com.me.library_service.model.request.AuthRequest;
import com.me.library_service.model.response.AuthResponse;
import com.me.library_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest authRequest) {
        log.debug("[+] AuthController:register with request dto: {}", authRequest);
        AuthResponse authResponse =  authService.register(authRequest);
        log.debug("[+] AuthController:register with response: {}", authResponse);
        return ResponseEntity.ok().body(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        log.debug("[+] AuthController:login with request dto: {}", authRequest);
        AuthResponse authResponse = authService.login(authRequest);
        log.debug("[+] AuthController:login with response: {}", authResponse);
        return ResponseEntity.ok().body(authResponse);
    }

}
