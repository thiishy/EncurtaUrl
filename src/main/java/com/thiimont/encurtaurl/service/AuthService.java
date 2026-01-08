package com.thiimont.encurtaurl.service;

import com.thiimont.encurtaurl.configuration.security.TokenConfig;
import com.thiimont.encurtaurl.dto.request.LoginRequestDTO;
import com.thiimont.encurtaurl.dto.request.RegisterRequestDTO;
import com.thiimont.encurtaurl.dto.response.LoginResponseDTO;
import com.thiimont.encurtaurl.dto.response.RegisterResponseDTO;
import com.thiimont.encurtaurl.exception.ResourceCreationException;
import com.thiimont.encurtaurl.model.User;
import com.thiimont.encurtaurl.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public LoginResponseDTO authenticateUser(String tokenCookie, LoginRequestDTO request, HttpServletResponse httpResponse) {
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        Authentication authentication = authenticationManager.authenticate(userAndPass);

        User user = (User) authentication.getPrincipal();
        String token = tokenConfig.generateToken(user);

        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(43200);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite", "Strict");
        cookie.setPath("/");
        httpResponse.addCookie(cookie);

        return new LoginResponseDTO(token);
    }

    public RegisterResponseDTO registerUser(RegisterRequestDTO request) {
        if(!request.password().equals(request.confirmPassword())) throw new ResourceCreationException("As senhas não coincidem.");

        User newUser = new User();
        newUser.setName(request.name());
        newUser.setUsername(request.username());
        newUser.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(newUser);
        return new RegisterResponseDTO(LocalDateTime.now(), HttpStatus.CREATED.name(), "Usuário registrado com sucesso.");
    }
}
