package com.thiimont.encurtaurl.service;

import com.thiimont.encurtaurl.configuration.UrlConfig;
import com.thiimont.encurtaurl.dto.UrlResponseDTO;
import com.thiimont.encurtaurl.model.Url;
import com.thiimont.encurtaurl.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import com.thiimont.encurtaurl.exception.InvalidUrlException;
import com.thiimont.encurtaurl.exception.UrlNotFoundException;

import java.net.URL;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UrlService {
    private final UrlRepository urlRepository;
    private final UrlConfig urlConfig;
    private final SecureRandom secureRandom;

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 6;

    public UrlService(UrlRepository urlRepository, UrlConfig urlConfig, SecureRandom secureRandom) {
        this.urlRepository = urlRepository;
        this.urlConfig = urlConfig;
        this.secureRandom = secureRandom;
    }

    public boolean isValidUrl(String targetUrl) {
        if (!targetUrl.isBlank()) {
            try {
                URL urlObj = new URL(targetUrl);
                urlObj.toURI();
                return true;
            } catch (MalformedURLException | URISyntaxException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public String generateShortCode() {
        String shortCode;

        do {
            shortCode = IntStream.generate(() -> secureRandom.nextInt(ALPHABET.length()))
                    .limit(LENGTH)
                    .mapToObj(i -> String.valueOf(ALPHABET.charAt(i)))
                    .collect(Collectors.joining());
        } while (urlRepository.existsByShortCode(shortCode));

        return shortCode;
    }

    public UrlResponseDTO shortenUrl(String targetUrl) {
        if (!isValidUrl(targetUrl)) {
            throw new InvalidUrlException();
        }

        Url url = new Url();

        String shortCode = generateShortCode();

        url.setTargetUrl(targetUrl);
        url.setShortenedUrl(urlConfig.getBaseUrl() + "/" + shortCode);
        url.setShortCode(shortCode);
        url.setCreatedAt(LocalDateTime.now());

        urlRepository.save(url);

        return new UrlResponseDTO(url.getShortenedUrl(), url.getCreatedAt());
    }

    public String getTargetUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .map(Url::getTargetUrl)
                .orElseThrow(UrlNotFoundException::new);
    }
}
