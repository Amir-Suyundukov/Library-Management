package com.syuyndukov.library.library_managemen.dto.lib;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookResponseDto {

    private Long id;
    private String title;
    private String isbn;
    private Integer publicationYear;
    private Integer copies;

    private Set<AuthorResponseDto> authors;
}
