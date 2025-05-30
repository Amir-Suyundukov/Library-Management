package com.syuyndukov.library.library_managemen.impl;

import com.syuyndukov.library.library_managemen.domain.lib.Author;
import com.syuyndukov.library.library_managemen.domain.lib.Book;
import com.syuyndukov.library.library_managemen.dto.lib.BookCreationDto;
import com.syuyndukov.library.library_managemen.dto.lib.BookResponseDto;
import com.syuyndukov.library.library_managemen.dto.lib.BookUpdateDto;
import com.syuyndukov.library.library_managemen.mapper.BookMapper;
import com.syuyndukov.library.library_managemen.repository.AuthorRepository;
import com.syuyndukov.library.library_managemen.repository.BookRepository;
import com.syuyndukov.library.library_managemen.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
    public BookResponseDto createBook(BookCreationDto bookDto) {
        Book book = bookMapper.toEntity(bookDto);

        Set<Author> authors = new HashSet<>();
        if (bookDto.getAuthorIds() == null && !bookDto.getAuthorIds().isEmpty()){
            authors = bookDto.getAuthorIds().stream().map(authorId -> authorRepository.findById(authorId)
                    .orElseThrow(() -> new RuntimeException("Author not found with id: " + authorId)))
                    .collect(Collectors.toSet());
        }
//        else {
//            throw new RuntimeException("У книги должно быть хотя бы 1 автор (BookServiceImpl.createBook)");
//        }
        authors.forEach(book::addAuthor);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public Optional<BookResponseDto> findById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDto);
    }

    @Override
    public Optional<BookResponseDto> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn).map(bookMapper::toDto);
    }

    @Override
    public List<BookResponseDto> searchBooks(String query) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(query);
        return bookMapper.toDtoList(books);
    }

    @Override
    public List<BookResponseDto> findAllBooks() {
        List<Book> books = bookRepository.findAll();
        return bookMapper.toDtoList(books);
    }

    @Override
    @Transactional
    public BookResponseDto updateBook(Long id, BookUpdateDto bookDetails) {
        Book bookToUpdate = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        bookMapper.updateEntityFromDto(bookDetails, bookToUpdate);

        if (bookDetails.getAuthorIds() == null || bookDetails.getAuthorIds().isEmpty()) {
            throw new RuntimeException("Book must have at least one author after update.");
        }

        Set<Author> authors = bookDetails.getAuthorIds().stream()
                .map(authorId -> authorRepository.findById(authorId)
                        .orElseThrow(() -> new RuntimeException("Author not found with id: " + authorId)))
                .collect(Collectors.toSet());

        bookToUpdate.getAuthors().clear();
        authors.forEach(bookToUpdate::addAuthor);

        Book saved = bookRepository.save(bookToUpdate);
        return bookMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
