package com.syuyndukov.library.library_managemen.impl;

import com.syuyndukov.library.library_managemen.domain.lib.Author;
import com.syuyndukov.library.library_managemen.dto.lib.AuthorCreationDto;
import com.syuyndukov.library.library_managemen.dto.lib.AuthorResponseDto;
import com.syuyndukov.library.library_managemen.dto.lib.AuthorUpdateDto;
import com.syuyndukov.library.library_managemen.mapper.AuthorMapper;
import com.syuyndukov.library.library_managemen.repository.AuthorRepository;
import com.syuyndukov.library.library_managemen.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public AuthorResponseDto createAuthor(AuthorCreationDto authorDto) {
        Author author = authorMapper.toEntity(authorDto);
        Author saveAuthor = authorRepository.save(author);
        return authorMapper.toDto(saveAuthor);
    }

    @Override
    public Optional<AuthorResponseDto> findById(Long id) {
        return authorRepository.findById(id).map(authorMapper::toDto);
    }

    @Override
    public Optional<AuthorResponseDto> findByFullName(String fullName) {
        return authorRepository.findByFullName(fullName).map(authorMapper::toDto);
    }

    @Override
    public List<AuthorResponseDto> searchByFullName(String fullNamePart) {
        List<Author> authors = authorRepository.findByFullNameContainingIgnoreCase(fullNamePart);
        return authorMapper.toDtoList(authors);
    }

    @Override
    public List<AuthorResponseDto> findAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authorMapper.toDtoList(authors);
    }

    @Override
    public AuthorResponseDto updateAuthor(Long id, AuthorUpdateDto authorDetails) {
        Author authorToUpdate = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));

        authorMapper.updateEntityFromDto(authorDetails, authorToUpdate);

        return authorMapper.toDto(authorToUpdate);
    }

    @Override
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}
