package com.syuyndukov.library.library_managemen.repository;

import com.syuyndukov.library.library_managemen.domain.lib.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByTitleContainingIgnoreCase(String titlePart);

    List<Book> findByPublicationYear(Integer year);

    // TODO: Возможно, добавить методы для поиска по автору?
    //  List<Book> findByAuthors_FullNameContainingIgnoreCase(String authorNamePart); // Пример синтаксиса для обхода по связи

    // TODO: Возможно, добавить методы для проверки наличия экземпляров?
    //  List<Book> findByCopiesGreaterThan(Integer minCopies);

}
