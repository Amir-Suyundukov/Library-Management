package com.syuyndukov.library.library_managemen.dto.lib;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthorUpdateDto {

    @NotBlank(message = "ФИО автора не может быть пустым при обновлении")
    @Size(max = 100, message = "ФИО автора не может превышать 100 символов")
    private String fullName;
}
