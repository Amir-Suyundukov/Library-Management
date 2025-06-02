package com.syuyndukov.library.library_managemen.dto.loan;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoanCreationDto {

    @NotNull(message = "ID книги не может быть пустым")
    private Long bookId;

    @NotNull(message = "ID пользователя не может быть пустым")
    private Long userId;
}
