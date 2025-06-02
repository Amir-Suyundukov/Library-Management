package com.syuyndukov.library.library_managemen.dto.loan;

import com.syuyndukov.library.library_managemen.dto.UserResponseDto;
import com.syuyndukov.library.library_managemen.dto.lib.BookResponseDto;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoanResponseDto {

    private Long id;

    // Информация о книге, которую выдали (используем уже существующий DTO)
    private BookResponseDto book;

    // Информация о пользователе, которому выдали книгу (используем уже существующий DTO)
    private UserResponseDto user;

    private LocalDate issueDate; //дата выдачи
    private LocalDate dueDate; //ожидаемая дата выдачи
    private LocalDate returnDate; //Фактическая дата выдачи (может быть null)

    private boolean isReturned; //возвращена ли книга
    private boolean isOverdue; //прострочена ли выдача
    private long DaysOverdue; //количество дней просрочки

    // TODO: Возможно, добавить поле для расчета штрафа
    //  private BigDecimal fineAmount;


}
