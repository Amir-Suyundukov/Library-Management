package com.syuyndukov.library.library_managemen.repository;

import com.syuyndukov.library.library_managemen.domain.User;
import com.syuyndukov.library.library_managemen.domain.lib.Book;
import com.syuyndukov.library.library_managemen.domain.loan.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    //Находит все выдачи для конкретной книги
    List<Loan> findByBook(Book book);

    // Находит все выдачи для конкретного пользователя (читателя)
    List<Loan> findByUser(User user);

    // Находит все активные выдачи (где returnDate == null)
    List<Loan> findByReturnDateIsNull();

    // Находит все возвращенные выдачи (где returnDate != null)
    List<Loan> findByReturnDateIsNotNull();

    // Находит все активные выдачи для конкретного пользователя (где user == User И returnDate == null)
    List<Loan> findByUserAndReturnDateIsNull();
}
