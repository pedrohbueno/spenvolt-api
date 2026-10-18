package com.spenvolt.api.controller;

import com.spenvolt.api.dto.request.UserRequestDTO;
import com.spenvolt.api.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody UserRequestDTO request) {
        return Map.of("message", authService.register(request));
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody UserRequestDTO request) {
        return Map.of("token", authService.login(
                request.getEmail(),
                request.getPassword()
        ));
    }
}
