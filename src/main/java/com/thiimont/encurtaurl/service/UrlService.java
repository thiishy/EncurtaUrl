package com.thiimont.encurtaurl.service;

import com.thiimont.encurtaurl.configuration.UrlConfig;
import com.thiimont.encurtaurl.dto.response.UrlResponseDTO;
import com.thiimont.encurtaurl.exception.InactiveUrlException;
import com.thiimont.encurtaurl.model.Url;
import com.thiimont.encurtaurl.model.UrlStatus;
import com.thiimont.encurtaurl.model.User;
import com.thiimont.encurtaurl.repository.UrlRepository;
import com.thiimont.encurtaurl.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.net.*;

import com.thiimont.encurtaurl.exception.InvalidUrlException;
import com.thiimont.encurtaurl.exception.UrlNotFoundException;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;
    private final UserRepository userRepository;
    private final UrlConfig urlConfig;
    private final SecureRandom secureRandom;
    private final UrlValidator urlValidator;

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 6;

    private static final int PAGE_SIZE = 10;

    private URI parseUriAndNormalize(String targetUrl) {
        try {
            URI uri = new URI(targetUrl);

            if (uri.getUserInfo() != null) throw new InvalidUrlException();

            String authority = uri.getAuthority();
            if (authority == null || authority.isBlank()) throw new InvalidUrlException();

            int colonIndex = authority.indexOf(':');
            String host = colonIndex >= 0 ? authority.substring(0, colonIndex) : authority;

            String asciiHost = IDN.toASCII(host);
            if (asciiHost.isBlank()) throw new InvalidUrlException();

            return new URI(
                    uri.getScheme(),
                    null,
                    asciiHost,
                    uri.getPort(),
                    uri.getPath(),
                    uri.getQuery(),
                    uri.getFragment()
            );

        } catch (URISyntaxException e) {
            throw new InvalidUrlException();
        }
    }

    private boolean isValidUrl(String normalizedUri) {
        if (normalizedUri == null || normalizedUri.isBlank()) return false;
        if (!urlValidator.isValid(normalizedUri)) return false;

        return true;
    }

    private String generateShortCode() {
        return IntStream.generate(() -> secureRandom.nextInt(ALPHABET.length()))
                .limit(LENGTH)
                .mapToObj(i -> String.valueOf(ALPHABET.charAt(i)))
                .collect(Collectors.joining());
    }

    public UrlResponseDTO shortenUrl(UUID uuidUser, String targetUrl) {
        User user = userRepository.findByUuid(uuidUser)
                .orElseThrow(() -> new AccessDeniedException("Acesso negado."));

        URI normalizedUri = parseUriAndNormalize(targetUrl);

        if (!isValidUrl(normalizedUri.toString())) {
            throw new InvalidUrlException();
        }

        // O loop é justificável, isso nunca vai passar de 1 tentativa (mas é sempre bom se precaver)
        while(true) {
            String shortCode = generateShortCode();

            Url url = new Url();
            url.setTargetUrl(normalizedUri.toString());
            url.setShortCode(shortCode);
            url.setUser(user);

            try {
                urlRepository.save(url);
                return new UrlResponseDTO(url.getUuid(), url.getTargetUrl(), urlConfig.getBaseUrl() + "/u/" + shortCode, url.getCreatedAt());
            } catch(DataIntegrityViolationException ignored) {}
        }

    }

    public String getTargetUrl(String shortCode) {
        if(!urlRepository.existsByShortCode(shortCode)) throw new UrlNotFoundException();
        if(!urlRepository.existsByStatus(UrlStatus.ACTIVE)) throw new InactiveUrlException();

        return urlRepository.findByShortCode(shortCode)
                .map(Url::getTargetUrl)
                .orElseThrow(UrlNotFoundException::new);
    }

    public Page<UrlResponseDTO> getAllUrls(UUID uuidUser, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
        Page<Url> urlPage = urlRepository.findAllByUserUuid(uuidUser, pageable);

        return urlPage.map(u -> new UrlResponseDTO(u.getUuid(), u.getTargetUrl(), urlConfig.getBaseUrl() + "/u/" + u.getShortCode(), u.getCreatedAt()));
    }

    public void deleteUrl(UUID uuidUser, UUID uuidUrl) {
        Url url = urlRepository.findByUuidAndUserUuid(uuidUrl, uuidUser)
                .orElseThrow(UrlNotFoundException::new);

        urlRepository.delete(url);
    }
}
