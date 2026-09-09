package com.auth.service;

import com.auth.model.dto.*;
import com.auth.model.entity.RefreshToken;
import com.auth.model.entity.Role;
import com.auth.model.entity.User;
import com.auth.producer.KafkaProducerService;
import com.auth.repository.UserRepository;
import com.auth.security.JwtService;
import com.auth.security.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaProducerService kafkaProducerService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void register (RegisterUserRq dto) {
        if (userRepository.existsByUsername(dto.getUsername().trim())) {
            throw new RuntimeException("Этот никнейм занят. Выберите другой!");
        }

        User registerUser = User.builder()
                .username(dto.getUsername().trim())
                .passwordHash(passwordEncoder.encode(dto.getPassword()).trim())
                .role(Role.USER)
                .build();

        User user = userRepository.save(registerUser);

        AuditEvent registerUserLog = AuditEvent.builder()
                .uuid(user.getId())
                .message("Зарегистрирован новый аккаунт '" + user.getUsername() + "'")
                .type(AuditEventType.USER_REGISTERED)
                .createTime(OffsetDateTime.now())
                .build();

        kafkaProducerService.sendAuditEvent(registerUserLog);
    }

    @Transactional
    public LoginRs login (LoginUserRq dto) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                dto.getUsername(),
                                dto.getPassword()
                        )
                );

        String accessToken = jwtService.generateToken(authentication.getName());

        User user = userRepository
                .findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден!"));

        String refreshToken = refreshTokenService.createRefreshToken(user);

        AuditEvent auditEvent = AuditEvent.builder()
                .uuid(user.getId())
                .message("Авторизовался пользователь '" + user.getUsername() +"'!")
                .type(AuditEventType.USER_LOGGED_IN)
                .createTime(OffsetDateTime.now())
                .build();

        kafkaProducerService.sendAuditEvent(auditEvent);

        return new LoginRs(accessToken, refreshToken);
    }

    @Transactional
    public void logout (String token) {
        try {
            RefreshToken refreshToken = refreshTokenService.validate(token);
            refreshTokenService.revoke(refreshToken);

            User user = refreshToken.getUser();

            AuditEvent auditEvent = AuditEvent.builder()
                    .uuid(user.getId())
                    .message("Деавторизовался пользователь '" + user.getUsername() + "'!")
                    .type(AuditEventType.USER_LOGGED_OUT)
                    .createTime(OffsetDateTime.now())
                    .build();

            kafkaProducerService.sendAuditEvent(auditEvent);
        } catch (Exception e) {
            log.error("Произошла ошибка: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}