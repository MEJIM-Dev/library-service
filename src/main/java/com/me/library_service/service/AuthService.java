package com.me.library_service.service;

import com.me.library_service.model.request.AuthRequest;
import com.me.library_service.model.response.AuthResponse;

public interface AuthService {

    AuthResponse register(AuthRequest authRequest);

    AuthResponse login(AuthRequest authRequest);

}
