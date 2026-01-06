package com.thiimont.encurtaurl.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UrlRequestDTO(@NotBlank(message = "A URL não pode estar vazia.")
                            @Size(max = 2048, message = "O limite de caracteres é 2048.") String targetUrl) {}