package com.syuyndukov.library.library_managemen.dto.lib;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthorCreationDto {

    @NotBlank(message = "ФИО не может быть пустым")
    @Size(max = 100, message = "ФИО не может быть больше 100 символов")
    private String fullName;
}
