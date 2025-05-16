package com.syuyndukov.library.library_managemen.repository;

import com.syuyndukov.library.library_managemen.domain.lib.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByFullName(String fullName);

    List<Author> findByFullNameContainingIgnoreCase(String fullNamePart);
}
