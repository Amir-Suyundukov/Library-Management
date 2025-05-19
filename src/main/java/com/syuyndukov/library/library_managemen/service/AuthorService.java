package com.syuyndukov.library.library_managemen.service;

import com.syuyndukov.library.library_managemen.dto.lib.AuthorCreationDto;
import com.syuyndukov.library.library_managemen.dto.lib.AuthorResponseDto;
import com.syuyndukov.library.library_managemen.dto.lib.AuthorUpdateDto;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    AuthorResponseDto createAuthor(AuthorCreationDto authorDto);

    Optional<AuthorResponseDto> findById(Long id);

    Optional<AuthorResponseDto> findByFullName(String fullName);

    List<AuthorResponseDto> searchByFullName(String fullNamePart);

    List<AuthorResponseDto> findAllAuthors();

    AuthorResponseDto updateAuthor(Long id, AuthorUpdateDto authorDetails);

    void deleteAuthor(Long id);
}
