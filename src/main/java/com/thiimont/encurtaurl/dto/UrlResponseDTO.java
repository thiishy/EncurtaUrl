package com.thiimont.encurtaurl.dto;

import java.time.LocalDateTime;

public record UrlResponseDTO(Long id, String shortenedUrl, LocalDateTime createdAt) {}
