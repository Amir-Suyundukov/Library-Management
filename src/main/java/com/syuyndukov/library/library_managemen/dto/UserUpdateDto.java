package com.syuyndukov.library.library_managemen.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserUpdateDto {

    @Email(message = "Некорректный формат Email")
    private String email;
    
    @Size(max = 50, message = "Имя не может превышать 50 символов")
    private String firstName;

    @Size(max = 50, message = "Фамилия не может превышать 50 символов")
    private String lastName;

    private boolean enabled;

}
