package com.auth.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRq {
    @NotBlank(message = "Никнейм обязателен!")
    @Length(message = "Максимальная длина никнейма 19!", max = 19, min = 3)
    private String username;
    @NotBlank(message = "Пароль обязателен!")
    @Length(message = "Минимальная длина пароля 9, максимальная 50!", max = 50, min = 9)
    private String password;
}