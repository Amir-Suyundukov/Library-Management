package com.syuyndukov.library.library_managemen.domain.lib;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Book> books = new HashSet<>();

    // --- Вспомогательные методы для управления связью (опционально на стороне Author) ---
    // Если захочешь управлять связью со стороны автора, нужны эти методы и двунаправленная связь
     public void addBook(Book book) {
         this.books.add(book);
         if (book.getAuthors() != null) {
             book.getAuthors().add(this); // Синхронизируем другую сторону
         }
     }

     public void removeBook(Book book) {
          this.books.remove(book);
          if (book.getAuthors() != null) {
             book.getAuthors().remove(this); // Синхронизируем другую сторону
          }
     }
}
