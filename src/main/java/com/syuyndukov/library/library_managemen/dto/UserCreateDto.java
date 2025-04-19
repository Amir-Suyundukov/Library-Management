package com.syuyndukov.library.library_managemen.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserCreateDto {

    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 3, max = 50, message = "Логин должен быть от 3 до 50 символов")
    private String username;

    @NotBlank(message = "пароль не может быть пустым")
    @Size(min = 6, message = "Пароль должен быть минимум от 6 символов")
    private String password;

    @Email(message = "ваш Email, необязательно")
    private String email;

    @NotBlank(message = "Имя не должно быть пустым")
    @Size(max = 50, message = "Имя не может быть больше 50 символов")
    private String firstName;

    @NotBlank(message = "Имя не должно быть пустым")
    @Size(max = 50, message = "Фамилия не должна быть больше 50 символов")
    private String lastName;
}
