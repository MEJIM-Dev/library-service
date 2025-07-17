package com.me.library_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private Set<String> roles;

}
