package com.thiimont.encurtaurl.controller;

import com.thiimont.encurtaurl.dto.request.UrlRequestDTO;
import com.thiimont.encurtaurl.dto.response.UrlResponseDTO;
import com.thiimont.encurtaurl.service.UrlService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/")
@Tag(name = "URLs", description = "Endpoints relacionados ao gerenciamento das URLs do usuário")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @SecurityRequirement(name = "cookieAuth")
    @GetMapping("/me/urls")
    public ResponseEntity<Page<UrlResponseDTO>> listUrls(@AuthenticationPrincipal UUID uuidUser,
                                                         @RequestParam(defaultValue = "0") int page) {
        Page<UrlResponseDTO> response = urlService.getAllUrls(uuidUser, page);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @SecurityRequirement(name = "cookieAuth")
    @DeleteMapping("/me/urls/delete/{uuid}")
    public ResponseEntity<UrlResponseDTO> deleteUrl(@AuthenticationPrincipal UUID uuidUser,
                                                    @PathVariable UUID uuid) {
        urlService.deleteUrl(uuidUser, uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @SecurityRequirement(name = "cookieAuth")
    @PostMapping("/me/shorten")
    public ResponseEntity<UrlResponseDTO> registerUrl(@AuthenticationPrincipal UUID uuidUser,
                                                      @RequestBody @Valid UrlRequestDTO request) {
        UrlResponseDTO response = urlService.shortenUrl(uuidUser, request.targetUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/u/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String targetUrl = urlService.getTargetUrl(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(targetUrl))
                .build();
    }
}
