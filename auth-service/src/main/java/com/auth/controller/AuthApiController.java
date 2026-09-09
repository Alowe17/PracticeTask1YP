package com.auth.controller;

import com.auth.model.dto.*;
import com.auth.security.RefreshTokenService;
import com.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/api")
@RequiredArgsConstructor
public class AuthApiController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser (@RequestBody @Valid RegisterUserRq registerUserRq) {
        userService.register(registerUserRq);
    }

    @PostMapping("/login")
    public LoginRs loginUser (@RequestBody @Valid LoginUserRq loginUserRq) {
        return userService.login(loginUserRq);
    }

    @PostMapping("/refresh")
    public AccessToken refreshToken (@RequestBody RefreshTokenRq refreshToken) {
        return refreshTokenService.generateAccessToken(refreshToken);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logoutUser (@RequestBody @Valid RefreshTokenRq refreshToken) {
        userService.logout(refreshToken.getRefreshToken());
    }
}