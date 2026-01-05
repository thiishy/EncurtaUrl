package com.thiimont.encurtaurl.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
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

    @org.hibernate.validator.constraints.UUID
    @Column(name = "uuid_url", nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "target_url", nullable = false, length = 2048)
    private String targetUrl;

    @Column(name = "short_code", unique = true, nullable = false, length = 16)
    private String shortCode;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "status", columnDefinition = "url_status", nullable = false)
    private UrlStatus status;

    @Column(name = "created_at", nullable = false)
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
