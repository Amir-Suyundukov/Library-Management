package com.syuyndukov.library.library_managemen.mapper;

import com.syuyndukov.library.library_managemen.domain.lib.Book;
import com.syuyndukov.library.library_managemen.dto.lib.AuthorResponseDto;
import com.syuyndukov.library.library_managemen.dto.lib.BookCreationDto;
import com.syuyndukov.library.library_managemen.dto.lib.BookResponseDto;
import com.syuyndukov.library.library_managemen.dto.lib.BookUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    private final AuthorMapper authorMapper;

    public BookMapper(AuthorMapper authorMapper) {
        this.authorMapper = authorMapper;
    }

    public Book toEntity(BookCreationDto dto){
        if (dto == null){
            return null;
        }
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPublicationYear(dto.getPublicationYear());
        book.setCopies(dto.getCopies());

        return book;
    }

    public BookResponseDto toDto(Book book){
        if (book == null) {
            return null;
        }
        BookResponseDto dto = new BookResponseDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setCopies(book.getCopies());

        if (book.getAuthors() != null){
            Set<AuthorResponseDto> authorsDto = book.getAuthors().stream()
                    .map(authorMapper::toDto)
                    .collect(Collectors.toSet());
            dto.setAuthors(authorsDto);
        }else {
            dto.setAuthors(new HashSet<>());
        }
        return dto;
    }

    public List<BookResponseDto> toDtoList(List<Book> books){
        if(books == null){
            return null;
        }
        return books.stream()
                .map(this::toDto)
                .toList();
    }

    // Метод для копирования данных из BookUpdateDto в СУЩЕСТВУЮЩУЮ сущность Book
    public void updateEntityFromDto(BookUpdateDto dto, Book book){
        if (dto == null || book == null) {
            return;
        }

        if (dto.getTitle() != null){
            book.setTitle(dto.getTitle());
        }
        if (dto.getPublicationYear() != null) {
            book.setPublicationYear(dto.getPublicationYear());
        }
        if (dto.getCopies() != null){
            book.setCopies(dto.getCopies());
        }
    }

}
