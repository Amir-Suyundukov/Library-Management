package com.syuyndukov.library.library_managemen.repository;

import com.syuyndukov.library.library_managemen.domain.User;
import com.syuyndukov.library.library_managemen.domain.lib.Book;
import com.syuyndukov.library.library_managemen.domain.loan.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    // --- Методы с JOIN FETCH для загрузки Loan вместе с Book и User ---

    // Находит выдачу по ID и загружает книгу и пользователя
    @Query("SELECT l FROM Loan l JOIN FETCH l.book JOIN FETCH l.user WHERE l.id = :id")
    Optional<Loan> findByIdWithBookAndUser(@Param("id") Long id);

    // Находит все выдачи и загружает книгу и пользователя
    @Query("SELECT l FROM Loan l JOIN FETCH l.book JOIN FETCH l.user")
    List<Loan> findAllWithBookAndUser();

    // Находит все активные выдачи (returnDate == null) и загружает книгу и пользователя
    @Query("SELECT l FROM Loan l JOIN FETCH l.book JOIN FETCH l.user WHERE l.returnDate IS NULL")
    List<Loan> findActiveLoansWithBookAndUser();

    // Находит выдачи для конкретного пользователя и загружает книгу и пользователя
    @Query("SELECT l FROM Loan l JOIN FETCH l.book JOIN FETCH l.user WHERE l.user = :user")
    List<Loan> findByUserWithBookAndUser(@Param("user") User user);

    // Находит активные выдачи для конкретного пользователя и загружает книгу и пользователя
    @Query("SELECT l FROM Loan l JOIN FETCH l.book JOIN FETCH l.user WHERE l.user = :user AND l.returnDate IS NULL")
    List<Loan> findActiveLoansByUserIdWithBookAndUser(@Param("user") User user);

    // Находит выдачи для конкретной книги и загружает книгу и пользователя
    @Query("SELECT l FROM Loan l JOIN FETCH l.book JOIN FETCH l.user WHERE l.book = :book")
    List<Loan> findByBookWithBookAndUser(@Param("book") Book book);

    // Находит активные выдачи для конкретной книги и загружает книгу и пользователя
    @Query("SELECT l FROM Loan l JOIN FETCH l.book JOIN FETCH l.user WHERE l.book = :book AND l.returnDate IS NULL")
    List<Loan> findActiveLoansByBookIdWithBookAndUser(@Param("book") Book book);

    // --- Оставляем стандартные findBy для использования в изменяющих методах сервиса (lend/return) ---
    // (В них сущности Book и User загружаются по ID отдельно перед созданием Loan, или инициализируются)

    //Находит все выдачи для конкретной книги
    List<Loan> findByBook(Book book);

    // Находит все выдачи для конкретного пользователя (читателя)
    List<Loan> findByUser(User user);

    // Находит все активные выдачи (где returnDate == null)
    List<Loan> findByReturnDateIsNull();

    // Находит все возвращенные выдачи (где returnDate != null)
    List<Loan> findByReturnDateIsNotNull();

    // Находит все активные выдачи для конкретного пользователя (где user == User И returnDate == null)
    List<Loan> findByUserAndReturnDateIsNull(User user);
}
