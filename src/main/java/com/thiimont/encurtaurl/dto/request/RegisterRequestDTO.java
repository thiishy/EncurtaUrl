package com.thiimont.encurtaurl.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(@NotBlank(message = "O nome é obrigatório.") String name,
                                 @NotBlank(message = "O email é obrigatório.") @Size(max = 255) @Email String username,
                                 @NotBlank(message = "A senha é obrigatória.") @Size(min = 8, max = 64, message = "Sua senha deve ter no mínimo 8 e no máximo 64 caracteres.") String password,
                                 @NotBlank(message = "Você deve digitar sua senha novamente.") @Size(min = 8, max = 64, message = "Sua senha deve ter no mínimo 8 e no máximo 64 caracteres.") String confirmPassword) {
}
