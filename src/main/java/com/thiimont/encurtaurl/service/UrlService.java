package com.thiimont.encurtaurl.service;

import com.thiimont.encurtaurl.configuration.UrlConfig;
import com.thiimont.encurtaurl.dto.UrlResponseDTO;
import com.thiimont.encurtaurl.model.Url;
import com.thiimont.encurtaurl.repository.UrlRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private static final int PAGE_SIZE = 10;

    public UrlService(UrlRepository urlRepository, UrlConfig urlConfig, SecureRandom secureRandom) {
        this.urlRepository = urlRepository;
        this.urlConfig = urlConfig;
        this.secureRandom = secureRandom;
    }

    public boolean isValidUrl(String targetUrl) {
        if (targetUrl == null || targetUrl.isBlank()) return false;

        try {
            URL url = new URL(targetUrl);
            url.toURI();

            String protocol = url.getProtocol().toLowerCase();
            if (!protocol.equals("http") && !protocol.equals("https")) return false;

            String host = url.getHost();
            if (host == null || host.isBlank()) return false;
            if (host.startsWith(".") || host.endsWith(".")) return false;
            if (!host.contains(".")) return false;

            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }



    public String generateShortCode() {
        return IntStream.generate(() -> secureRandom.nextInt(ALPHABET.length()))
                .limit(LENGTH)
                .mapToObj(i -> String.valueOf(ALPHABET.charAt(i)))
                .collect(Collectors.joining());
    }

    public UrlResponseDTO shortenUrl(String targetUrl) {
        if (!isValidUrl(targetUrl)) {
            throw new InvalidUrlException();
        }

        // O loop é justificável, isso nunca vai passar de 1 tentativa (mas é sempre bom se precaver)
        while(true) {
            String shortCode = generateShortCode();

            Url url = new Url();
            url.setTargetUrl(targetUrl);
            url.setShortCode(shortCode);
            url.setCreatedAt(LocalDateTime.now());

            try {
                urlRepository.save(url);
                return new UrlResponseDTO(url.getId(), url.getTargetUrl(), urlConfig.getBaseUrl() + "/" + shortCode, url.getCreatedAt());
            } catch(DataIntegrityViolationException e) {
                continue;
            }
        }

    }

    public String getTargetUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .map(Url::getTargetUrl)
                .orElseThrow(UrlNotFoundException::new);
    }

    public Page<UrlResponseDTO> getAllUrls(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
        Page<Url> urlPage = urlRepository.findAll(pageable);

        return urlPage.map(u -> new UrlResponseDTO(u.getId(), u.getTargetUrl(), urlConfig.getBaseUrl() + "/" + u.getShortCode(), u.getCreatedAt()));
    }

    public void deleteUrl(Long id) {
        Url url = urlRepository.findById(id)
                .orElseThrow(UrlNotFoundException::new);

        urlRepository.delete(url);
    }
}
