package com.auth.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRq {
    @NotBlank(message = "Никнейм обязателен!")
    @Max(message = "Максимальная длина никнейма 19!", value = 19)
    @Min(message = "Минимальная длина никнейма 3!", value = 3)
    private String username;
    @NotBlank(message = "Пароль обязателен!")
    @Max(message = "Максимальная длина пароля 50!", value = 50)
    @Min(message = "Минимальная длина пароля 9!", value = 9)
    private String password;
}