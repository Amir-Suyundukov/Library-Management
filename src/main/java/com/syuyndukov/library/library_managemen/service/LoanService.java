package com.syuyndukov.library.library_managemen.service;

import com.syuyndukov.library.library_managemen.dto.loan.LoanCreationDto;
import com.syuyndukov.library.library_managemen.dto.loan.LoanResponseDto;

import java.util.List;
import java.util.Optional;

public interface LoanService {

    // Оформить новую выдачу книги
    // Принимает ID книги и ID пользователя
    LoanResponseDto lendBook(LoanCreationDto loanDto);

    // Оформить возврат книги
    // Принимает ID записи о выдаче
    LoanResponseDto returnBook(Long loanId);

    // Найти выдачу по ID
    Optional<LoanResponseDto> findById(Long loanId);

    // Получить список всех выдач (история + активные)
    // Доступно, например, ADMIN или LIBRARIAN
    List<LoanResponseDto> findAllLoans();

    // Получить список всех активных выдач (книги на руках)
    // Доступно, например, ADMIN или LIBRARIAN
    List<LoanResponseDto> findActiveLoans();

    // Получить список выдач для конкретного пользователя (история + активные)
    // Доступно, например, ADMIN, LIBRARIAN или самому Пользователю
    List<LoanResponseDto> findLoansByUserId(Long userId);

    // Получить список активных выдач для конкретного пользователя (его книги на руках)
    // Доступно, например, ADMIN, LIBRARIAN или самому Пользователю (для "Мои книги")
    List<LoanResponseDto> findActiveLoansByUserId(Long userId);

    // Получить список выдач для конкретной книги (история + активные)
    // Доступно, например, ADMIN или LIBRARIAN
    List<LoanResponseDto> findLoansByBookId(Long bookId);

    // Получить список активных выдач для конкретной книги (кто ее сейчас читает)
    // Доступно, например, ADMIN или LIBRARIAN
    List<LoanResponseDto> findActiveLoansByBookId(Long bookId);
}
