package com.auth.security;

import com.auth.model.dto.AccessToken;
import com.auth.model.dto.RefreshTokenRq;
import com.auth.model.entity.RefreshToken;
import com.auth.model.entity.User;
import com.auth.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final JwtService jwtService;

    @Transactional
    public String createRefreshToken (User user) {
        String token = generateRandomToken();

        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(token)
                .user(user)
                .createdAt(OffsetDateTime.now())
                .expiresAt(OffsetDateTime.now().plusDays(30))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);
        return token;
    }

    @Transactional
    public RefreshToken validate (String token) throws Exception {
        RefreshToken refreshToken = refreshTokenRepository
                .findByTokenHash(hash(token))
                .orElseThrow(() -> {
                    log.error("Не удалось найти нужный ттокен в базе данных");
                    return new RuntimeException("Не удалось проверить данные!");
                });


        if (refreshToken.isRevoked()) {
            throw new RuntimeException("Токен был отозван!");
        }

        if (refreshToken.getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new RuntimeException("Сессия истекла!");
        }

        return refreshToken;
    }

    @Transactional
    public void revoke (RefreshToken refreshToken) throws Exception {
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    public String generateRandomToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

    public String hash (String token) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(token.getBytes(StandardCharsets.UTF_8));

        StringBuilder result = new StringBuilder();

        for (byte b : hash) {
            result.append(String.format("%02x", b));
        }

        return result.toString();
    }

    public AccessToken generateAccessToken (RefreshTokenRq refreshTokenRq) {
        RefreshToken refreshToken;

        try {
            refreshToken = validate(refreshTokenRq.getRefreshToken());
        } catch (Exception e) {
            log.error("Ошибка проверки токена: " + e.getMessage());
            throw new RuntimeException("Не удалось проверить данные!");
        }

        String accessToken = jwtService.generateToken(refreshToken.getUser().getUsername());

        return new AccessToken(accessToken);
    }
}