package com.thiimont.encurtaurl.infrastructure;

import java.time.LocalDateTime;

public record RestErrorMessage(LocalDateTime timestamp, String status, String message) {}
