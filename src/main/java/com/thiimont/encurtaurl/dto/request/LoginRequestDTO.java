package com.thiimont.encurtaurl.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(@NotBlank(message = "O email é obrigatório.") String username,
                              @NotBlank(message = "A senha é obrigatória.") String password) {
}