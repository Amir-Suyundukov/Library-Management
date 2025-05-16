package com.syuyndukov.library.library_managemen.dto.lib;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookUpdateDto {

    @Size(max = 200 ,message = "Название книги не может превышать 200 символов")
    private String title;

    @Min(value = 0, message = "Год издания должен быть положительным числом")
    private Integer publicationYear;

    @NotNull(message = "Количество экземпляров не может быть пустым")
    @Min(value = 0, message = "Количество экземпляров не может быть отрицательным")
    private Integer copies;

    private Set<Long> authorsIds = new HashSet<>();
}
