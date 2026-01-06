package com.thiimont.encurtaurl.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.util.UUID;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "urls")
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_url")
    private Long id;

    @Generated(event = EventType.INSERT)
    @Column(name = "uuid_url", nullable = false, updatable = false, unique = true)
    private UUID uuid;

    @Column(name = "target_url", nullable = false, length = 2048)
    private String targetUrl;

    @Column(name = "short_code", unique = true, nullable = false, length = 16)
    private String shortCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 128)
    private UrlStatus status = UrlStatus.ACTIVE;

    @Generated(event = EventType.INSERT)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    public Url(String targetUrl, String shortCode, UrlStatus status) {
        this.targetUrl = targetUrl;
        this.shortCode = shortCode;
        this.status = status;
    }
}
