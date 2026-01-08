package com.thiimont.encurtaurl.dto.response;

import java.time.LocalDateTime;

public record RegisterResponseDTO(LocalDateTime timestamp, String status, String message) {
}
