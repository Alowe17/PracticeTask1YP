package com.auth.service;

import com.auth.model.dto.RegisterUserLog;
import com.auth.model.dto.RegisterUserRq;
import com.auth.model.dto.TypeLog;
import com.auth.model.entity.Role;
import com.auth.model.entity.User;
import com.auth.producer.KafkaProducerService;
import com.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaProducerService kafkaProducerService;

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

        RegisterUserLog registerUserLog = RegisterUserLog.builder()
                .uuid(user.getId())
                .message("Зарегистрирован новый аккаунт '" + user.getUsername() + "'")
                .type(TypeLog.REGISTER_USER)
                .build();

        kafkaProducerService.sendLogRegister(registerUserLog);
    }
}