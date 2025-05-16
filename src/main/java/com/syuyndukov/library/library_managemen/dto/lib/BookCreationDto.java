package com.syuyndukov.library.library_managemen.dto.lib;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class BookCreationDto {

    @NotBlank(message = "Название не может быть пустым")
    @Size(max = 200, message = "Размер названия книги не может превышать 200 символов")
    private String title;

    @NotBlank(message = "ISBN не может быть пустым")
    @Size(max = 20, message = "ISBN не может превышать 20 символов")
    private String isbn;

    @NotNull(message = "Год издания не может быть пустым")
    @Min(value = 0, message = "Год издания должен быть положительным числом")
    private Integer publicationYear;

    @NotNull(message = "Количество экземпляров не может быть пустым")
    @Min(value = 0, message = "Количество экземпляров не может быть отрицательным")
    private Integer copies;

    @NotNull(message = "Должен быть указан хотя бы один автор") // Книга должна иметь автора
    @Size(min = 1, message = "Должен быть указан хотя бы один автор")
    private Set<Long> authorsIds = new HashSet<>();


}
