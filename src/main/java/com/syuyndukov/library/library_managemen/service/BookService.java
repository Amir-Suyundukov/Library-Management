package com.syuyndukov.library.library_managemen.service;

import com.syuyndukov.library.library_managemen.dto.lib.BookCreationDto;
import com.syuyndukov.library.library_managemen.dto.lib.BookResponseDto;
import com.syuyndukov.library.library_managemen.dto.lib.BookUpdateDto;

import java.util.List;
import java.util.Optional;

public interface BookService {

    BookResponseDto createBook(BookCreationDto bookDto);

    Optional<BookResponseDto> findById(Long id);

    Optional<BookResponseDto> findByIsbn(String isbn);

    // TODO: Уточнить, какой именно поиск нужен в рамках базового функционала ТЗ
    List<BookResponseDto> searchBooks(String query); // Искать по названию ИЛИ по автору

    List<BookResponseDto> findAllBooks();

    BookResponseDto updateBook(Long id, BookUpdateDto bookDetails);

    void deleteBook(Long id);
}
