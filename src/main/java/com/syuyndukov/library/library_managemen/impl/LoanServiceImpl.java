package com.syuyndukov.library.library_managemen.impl;

import com.syuyndukov.library.library_managemen.domain.User;
import com.syuyndukov.library.library_managemen.domain.lib.Book;
import com.syuyndukov.library.library_managemen.domain.loan.Loan;
import com.syuyndukov.library.library_managemen.dto.loan.LoanCreationDto;
import com.syuyndukov.library.library_managemen.dto.loan.LoanResponseDto;
import com.syuyndukov.library.library_managemen.exeption.*;
import com.syuyndukov.library.library_managemen.mapper.LoanMapper;
import com.syuyndukov.library.library_managemen.repository.BookRepository;
import com.syuyndukov.library.library_managemen.repository.LoanRepository;
import com.syuyndukov.library.library_managemen.repository.UserRepository;
import com.syuyndukov.library.library_managemen.service.LoanService;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanMapper loanMapper;

    public LoanServiceImpl(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository, LoanMapper loanMapper) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.loanMapper = loanMapper;
    }

    /**
     * Оформляет новую выдачу книги читателю.
     * Проверяет наличие экземпляров, уменьшает их количество у книги, создает запись о выдаче.
     *
     * @param loanDto DTO с ID книги и пользователя.
     * @return DTO созданной записи о выдаче.
     */
    @Override
    @Transactional
    public LoanResponseDto lendBook(LoanCreationDto loanDto) {

        Book book = bookRepository.findById(loanDto.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Книга с ID:" + loanDto.getBookId() + " не найдена"));

        User user = userRepository.findById(loanDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID: " + loanDto.getUserId() + " не найдена"));

        if (book.getCopies() <= 0){
            throw new BusinessLogicException("книга '" + book.getTitle() + "' недоступна для выдачи (нет свободных экземпляров");
        }

        book.setCopies(book.getCopies() - 1);
        bookRepository.save(book);

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusWeeks(2);

        Loan newLoan = new Loan(book, user, issueDate, dueDate);
        Loan saveLoan = loanRepository.save(newLoan);

        return loanMapper.toDto(saveLoan);
    }

    /**
     * Оформляет возврат книги по записи о выдаче.
     * Проверяет, что книга не возвращена, проставляет дату возврата, увеличивает количество экземпляров у книги.
     *
     * @param loanId ID записи о выдаче.
     * @return DTO обновленной записи о выдаче.
     */
    @Override
    @Transactional
    public LoanResponseDto returnBook(Long loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("запись о выдачи с ID '" + loanId + "' не найдена"));

        Hibernate.initialize(loan.getBook());
        Hibernate.initialize(loan.getUser());

        if (loan.isReturned()){
            throw new LoanAlreadyReturnedException("Книга по выдачи с ID'" + loanId + "' уже была возвращена");
        }

        loan.setReturnDate(LocalDate.now());

        Book book = loan.getBook();

        book.setCopies(book.getCopies() + 1);
        bookRepository.save(book);
        return loanMapper.toDto(loan);
    }

    /**
     * Находит выдачу по ID.
     * Использует метод репозитория с JOIN FETCH для загрузки связанных Book и User.
     *
     * @param loanId ID выдачи.
     * @return Optional с DTO выдачи, если найден.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LoanResponseDto> findById(Long loanId) {

        Optional<Loan> loanOptional = loanRepository.findByIdWithBookAndUser(loanId);

        return loanOptional.map(loanMapper::toDto);
    }

    /**
     * Получить список всех выдач (история + активные).
     * Использует метод репозитория с JOIN FETCH.
     *
     * @return Список DTO всех выдач.
     */
    @Override
    @Transactional(readOnly = true)
    public List<LoanResponseDto> findAllLoans() {

        List<Loan> loans = loanRepository.findAllWithBookAndUser();

        return loanMapper.toDtoList(loans);
    }

    /**
     * Получить список всех активных выдач (книги на руках).
     * Использует метод репозитория с JOIN FETCH.
     *
     * @return Список DTO активных выдач.
     */
    @Override
    @Transactional(readOnly = true)
    public List<LoanResponseDto> findActiveLoans() {

        List<Loan> loans = loanRepository.findActiveLoansWithBookAndUser();

        return loanMapper.toDtoList(loans);
    }

    /**
     * Получить список выдач для конкретного пользователя (история + активные).
     * Использует метод репозитория с JOIN FETCH.
     *
     * @param userId ID пользователя.
     * @return Список DTO выдач пользователя.
     */
    @Override
    public List<LoanResponseDto> findLoansByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID '" + userId + "' не найден"));

        List<Loan> loans = loanRepository.findByUserWithBookAndUser(user);

        return loanMapper.toDtoList(loans);
    }

    /**
     * Получить список активных выдач для конкретного пользователя (его книги на руках).
     * Использует метод репозитория с JOIN FETCH.
     *
     * @param userId ID пользователя.
     * @return Список DTO активных выдач пользователя.
     */
    @Override
    @Transactional(readOnly = true)
    public List<LoanResponseDto> findActiveLoansByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID: '" + userId + "' не найден"));

        List<Loan> loans = loanRepository.findActiveLoansByUserIdWithBookAndUser(user);

        return loanMapper.toDtoList(loans);
    }

    /**
     * Получить список выдач для конкретной книги (история + активные).
     * Использует метод репозитория с JOIN FETCH.
     *
     * @param bookId ID книги.
     * @return Список DTO выдач книги.
     */
    @Override
    @Transactional(readOnly = true)
    public List<LoanResponseDto> findLoansByBookId(Long bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Книга с ID: '" + bookId + "' не найдена"));

        List<Loan> loans = loanRepository.findByBookWithBookAndUser(book);

        return loanMapper.toDtoList(loans);
    }

    /**
     * Получить список активных выдач для конкретной книги (кто ее сейчас читает).
     * Использует метод репозитория с JOIN FETCH.
     *
     * @param bookId ID книги.
     * @return Список DTO активных выдач книги.
     */
    @Override
    @Transactional(readOnly = true)
    public List<LoanResponseDto> findActiveLoansByBookId(Long bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Книга с ID: '" + bookId + "' не найдена"));

        List<Loan> loans = loanRepository.findActiveLoansByBookIdWithBookAndUser(book);

        return loanMapper.toDtoList(loans);
    }
}
