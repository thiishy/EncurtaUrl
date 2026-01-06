package com.thiimont.encurtaurl.configuration.security;

import lombok.Builder;

import java.util.UUID;

@Builder
public record JWTUserData(UUID uuid, String username) {
}
