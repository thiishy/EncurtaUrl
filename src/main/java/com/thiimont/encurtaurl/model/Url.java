package com.thiimont.encurtaurl.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String targetUrl;

    @Column(unique = true, nullable = false)
    private String shortenedUrl;

    @Column(unique = true, nullable = false)
    private String shortCode;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Url(String targetUrl, String shortenedUrl, String shortCode, LocalDateTime createdAt) {
        this.targetUrl = targetUrl;
        this.shortenedUrl = shortenedUrl;
        this.shortCode = shortCode;
        this.createdAt = createdAt;
    }
}
