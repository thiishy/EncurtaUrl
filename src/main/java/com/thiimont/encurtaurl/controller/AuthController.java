package com.thiimont.encurtaurl.controller;

import com.thiimont.encurtaurl.dto.request.LoginRequestDTO;
import com.thiimont.encurtaurl.dto.request.RegisterRequestDTO;
import com.thiimont.encurtaurl.dto.response.LoginResponseDTO;
import com.thiimont.encurtaurl.dto.response.RegisterResponseDTO;
import com.thiimont.encurtaurl.repository.UserRepository;
import com.thiimont.encurtaurl.service.AuthService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Endpoints relacionados ao login e registro de usuários")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@CookieValue(value = "token", required = false) @Parameter(hidden = true) String tokenCookie,
                                                  @Valid @RequestBody LoginRequestDTO request,
                                                  HttpServletResponse httpResponse) {
        LoginResponseDTO response = authService.authenticateUser(tokenCookie, request, httpResponse);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        RegisterResponseDTO response = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
