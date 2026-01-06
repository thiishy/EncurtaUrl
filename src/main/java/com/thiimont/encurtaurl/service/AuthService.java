package com.thiimont.encurtaurl.service;

import com.thiimont.encurtaurl.configuration.security.TokenConfig;
import com.thiimont.encurtaurl.dto.request.LoginRequestDTO;
import com.thiimont.encurtaurl.dto.request.RegisterRequestDTO;
import com.thiimont.encurtaurl.dto.response.LoginResponseDTO;
import com.thiimont.encurtaurl.dto.response.RegisterResponseDTO;
import com.thiimont.encurtaurl.model.User;
import com.thiimont.encurtaurl.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public LoginResponseDTO authenticateUser(LoginRequestDTO request) {
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        Authentication authentication = authenticationManager.authenticate(userAndPass);

        User user = (User) authentication.getPrincipal();
        String token = tokenConfig.generateToken(user);

        return new LoginResponseDTO(token);
    }

    public RegisterResponseDTO registerUser(RegisterRequestDTO request) {
        if(!request.password().equals(request.confirmPassword())) throw new IllegalArgumentException("As senhas não coincidem.");

        User newUser = new User();
        newUser.setName(request.name());
        newUser.setUsername(request.username());
        newUser.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(newUser);
        return new RegisterResponseDTO(request.name(), request.username());
    }
}
