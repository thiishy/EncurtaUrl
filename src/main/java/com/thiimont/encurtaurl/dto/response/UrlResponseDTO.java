package com.thiimont.encurtaurl.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record UrlResponseDTO(UUID uuid, String targetUrl, String shortenedUrl, LocalDateTime createdAt) {}
