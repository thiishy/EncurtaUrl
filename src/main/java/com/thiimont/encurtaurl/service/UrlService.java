package com.thiimont.encurtaurl.service;

import com.thiimont.encurtaurl.dto.response.UrlResponseDTO;
import com.thiimont.encurtaurl.exception.InactiveUrlException;
import com.thiimont.encurtaurl.exception.ResourceCreationException;
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
    private final SecureRandom secureRandom;
    private final UrlValidator urlValidator;

    private static final int MAX_SHORTCODE_ATTEMPTS = 5;

    private static final String SHORTCODE_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORTCODE_LENGTH = 6;

    private static final int PAGE_SIZE = 10;

    private String getUrlHostAndPort(String baseUrl) {
        try {
            URL url = new URL(baseUrl);
            return "https://" + url.getAuthority();
        } catch(MalformedURLException e) {
            throw new ResourceCreationException();
        }
    }

    private URI parseUriAndNormalize(String targetUrl) {
        try {
            URI uri;

            if (!targetUrl.matches("(?i)^https?://.*")) {
                uri = new URI("https://" + targetUrl);
            } else {
                uri = new URI(targetUrl.replaceFirst("(?i)^https?://", "https://"));
            }

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
        return IntStream.generate(() -> secureRandom.nextInt(SHORTCODE_ALPHABET.length()))
                .limit(SHORTCODE_LENGTH)
                .mapToObj(i -> String.valueOf(SHORTCODE_ALPHABET.charAt(i)))
                .collect(Collectors.joining());
    }

    public UrlResponseDTO shortenUrl(UUID uuidUser, String targetUrl, String baseUrl) {
        int attempts = 0;

        User user = userRepository.findByUuid(uuidUser)
                .orElseThrow(() -> new AccessDeniedException("Acesso negado."));

        String normalizedUri = parseUriAndNormalize(targetUrl).toString().trim();
        if (!isValidUrl(normalizedUri)) throw new InvalidUrlException();

        while (attempts < MAX_SHORTCODE_ATTEMPTS) {
            attempts++;

            String shortCode = generateShortCode();

            Url url = new Url();
            url.setTargetUrl(normalizedUri);
            url.setShortCode(shortCode);
            url.setUser(user);

            try {
                urlRepository.save(url);

                return new UrlResponseDTO(
                        url.getUuid(),
                        url.getTargetUrl(),
                        getUrlHostAndPort(baseUrl) + "/u/" + shortCode,
                        url.getCreatedAt()
                );
            } catch (DataIntegrityViolationException ignored) {}
        }

        throw new ResourceCreationException("Não foi possível gerar um shortCode único após 5 tentativas.");
    }

    public String getTargetUrl(String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(UrlNotFoundException::new);

        if(url.getStatus() != UrlStatus.ACTIVE) {
            throw new InactiveUrlException();
        }

        return url.getTargetUrl();
    }

    public Page<UrlResponseDTO> getAllUrls(UUID uuidUser, int page, String baseUrl) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
        Page<Url> urlPage = urlRepository.findAllByUserUuid(uuidUser, pageable);

        return urlPage.map(u ->
                new UrlResponseDTO(
                        u.getUuid(),
                        u.getTargetUrl(),
                        getUrlHostAndPort(baseUrl) + "/u/" + u.getShortCode(),
                        u.getCreatedAt()));
    }

    public void deleteUrl(UUID uuidUser, UUID uuidUrl) {
        Url url = urlRepository.findByUuidAndUserUuid(uuidUrl, uuidUser)
                .orElseThrow(UrlNotFoundException::new);

        urlRepository.delete(url);
    }
}
