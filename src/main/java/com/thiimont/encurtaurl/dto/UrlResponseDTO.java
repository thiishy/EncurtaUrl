package com.thiimont.encurtaurl.dto;

import java.time.LocalDateTime;

public record UrlResponseDTO(String shortenedUrl, LocalDateTime createdAt) {}
