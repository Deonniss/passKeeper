package ru.golovin.passkeeper.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequest {

    @Size(min = 2, max = 32, message = "Имя пользователя должно содержать от 2 до 32 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;

    @Size(min = 3, max = 255, message = "Длина пароля должна быть от 3 до 255 символов")
    @NotBlank(message = "Пароль не может быть пустыми")
    private String password;
}
