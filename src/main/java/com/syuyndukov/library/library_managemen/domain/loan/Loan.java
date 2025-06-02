package com.syuyndukov.library.library_managemen.domain.loan;

import com.syuyndukov.library.library_managemen.domain.User;
import com.syuyndukov.library.library_managemen.domain.lib.Book;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "loans")
public class Loan {

    public Loan(Book book, User user, LocalDate issueDate, LocalDate dueDate) {
        this.book = book;
        this.user = user;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = null; // При создании returnDate всегда null
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //1 книга может много раз выдоватся
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)// Колонка в таблице loans, которая хранит ID книги. Не может быть null.
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Book book;

    //один читатель может иметь много книг
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_Date")
    private LocalDate returnDate;

    //----МЕТОДЫ ДЛЯ ПРОВЕРКИ СТАТУСА----//

    /**
     * Проверяет, возвращена ли книга.
     * Книга считается возвращенной, если у выдачи проставлена фактическая дата возврата (returnDate).
     * @return true, если книга возвращена, иначе false.
     */
    public boolean isReturned(){
        return this.returnDate != null;
    }

    /**
     * Проверяет, просрочена ли выдача.
     * Выдача считается просроченной, если книга еще не возвращена
     * и текущая дата НАСТУПИЛА после ожидаемой даты возврата (dueDate).
     * @return true, если выдача просрочена, иначе false.
     */
    public boolean isOverdue(){
        if(isReturned()){
            return false;
        }
        return LocalDate.now().isAfter(this.dueDate);
    }

    /**
     * Возвращает количество дней просрочки.
     * Актуально только для просроченных и еще не возвращенных выдач.
     * @return Количество дней просрочки, или 0, если выдача не просрочена или уже возвращена.
     */
    public Long getDaysOverdue(){
        if (!isReturned()){
            return 0L;
        }
        return ChronoUnit.DAYS.between(this.dueDate, LocalDate.now());
    }
}
