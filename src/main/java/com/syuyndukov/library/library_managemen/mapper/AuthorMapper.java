package com.syuyndukov.library.library_managemen.mapper;

import com.syuyndukov.library.library_managemen.domain.lib.Author;
import com.syuyndukov.library.library_managemen.dto.lib.AuthorCreationDto;
import com.syuyndukov.library.library_managemen.dto.lib.AuthorResponseDto;
import com.syuyndukov.library.library_managemen.dto.lib.AuthorUpdateDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthorMapper {

    public Author toEntity(AuthorCreationDto dto){
        if (dto == null){
            return null;
        }
        Author author = new Author();
        author.setFullName(dto.getFullName());

        return author;
    }

    public AuthorResponseDto toDto(Author author){
        if (author == null){
            return null;
        }
        AuthorResponseDto dto = new AuthorResponseDto();
        dto.setId(author.getId());
        dto.setFullName(author.getFullName());

        // TODO: Если захочешь отображать список книг автора в AuthorResponseDto,
        // TODO: нужно будет маппить их здесь (и учесть LAZY/EAGER загрузку)
        //  Например:
        //  if (author.getBooks() != null) {
        //     dto.setBookTitles(author.getBooks().stream()
        //                                  .map(Book::getTitle) // Преобразуем Book в String (title)
        //                                  .collect(Collectors.toSet()));
        //  }

        return dto;
    }

    public List<AuthorResponseDto> toDtoList(List<Author> authors){
        if (authors == null){
            return null;
        }
        return authors.stream()
                .map(this::toDto)
                .toList();
    }

    public Set<AuthorResponseDto> toDtoSet(Set<Author> authors){
        if (authors == null){
            return null;
        }
        return authors.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }


    public void updateEntityFromDto(AuthorUpdateDto dto, Author author) {
        if (dto == null || author == null){
            return;
        }

        if (dto.getFullName() != null){
            author.setFullName(dto.getFullName());
        }
    }

}
