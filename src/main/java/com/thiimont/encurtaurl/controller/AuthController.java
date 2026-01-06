package com.thiimont.encurtaurl.controller;

import com.thiimont.encurtaurl.dto.request.LoginRequestDTO;
import com.thiimont.encurtaurl.dto.request.RegisterRequestDTO;
import com.thiimont.encurtaurl.dto.response.LoginResponseDTO;
import com.thiimont.encurtaurl.dto.response.RegisterResponseDTO;
import com.thiimont.encurtaurl.repository.UserRepository;
import com.thiimont.encurtaurl.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Endpoints relacionados ao login e registro de usuários")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.authenticateUser(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        RegisterResponseDTO response = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
