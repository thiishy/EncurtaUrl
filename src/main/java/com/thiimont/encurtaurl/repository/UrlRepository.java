package com.thiimont.encurtaurl.repository;

import com.thiimont.encurtaurl.model.Url;
import com.thiimont.encurtaurl.model.UrlStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByUuidAndUserUuid(UUID uuidUrl, UUID uuidUser);
    Page<Url> findAllByUserUuid(UUID uuidUser, Pageable pageable);
    Optional<Url> findByShortCode(String shortCode);
}
