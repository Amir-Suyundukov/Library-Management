package com.syuyndukov.library.library_managemen.dto.lib;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthorResponseDto {

    private Long id;
    private String fullName;
    // TODO: Возможно, добавить список названий книг этого автора позже
    //  private Set<String> bookTitles;
}
