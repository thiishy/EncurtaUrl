package com.thiimont.encurtaurl.controller;

import com.thiimont.encurtaurl.dto.UrlRequestDTO;
import com.thiimont.encurtaurl.dto.UrlResponseDTO;
import com.thiimont.encurtaurl.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/urls")
    public ResponseEntity<List<UrlResponseDTO>> listUrls() {
        List<UrlResponseDTO> response = urlService.getAllUrls();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UrlResponseDTO> registerUrl(@RequestBody UrlRequestDTO request) {
        UrlResponseDTO response = urlService.shortenUrl(request.targetUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UrlResponseDTO> deleteUrl(@PathVariable Long id) {
        urlService.deleteUrl(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String targetUrl = urlService.getTargetUrl(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(targetUrl))
                .build();
    }
}
