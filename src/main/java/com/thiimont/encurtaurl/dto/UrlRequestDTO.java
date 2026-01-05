package com.thiimont.encurtaurl.dto;

import jakarta.validation.constraints.NotBlank;

public record UrlRequestDTO(@NotBlank(message = "A URL não pode estar vazia.") String targetUrl) {}