package com.syuyndukov.library.library_managemen.domain.lib;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SecondaryRow;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "authors")
@ToString(exclude = "authors")
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(name = "publicationYear")
    private Integer publicationYear;

    @Column(nullable = false)
    private Integer copies;

    // --- Связь Many-to-Many с Author ---
    // Это УПРАВЛЯЮЩАЯ сторона связи. Здесь определяется промежуточная таблица.
    @ManyToMany(fetch = FetchType.LAZY) // LAZY - не загружать авторов сразу
    @JoinTable( // Описываем промежуточную таблицу для связи
            name = "book_authors", // Название промежуточной таблицы (можно выбрать другое)
            joinColumns = @JoinColumn(name = "book_id"), // Колонка в промежуточной таблице, ссылающаяся на ЭТУ сущность (Book)
            inverseJoinColumns = @JoinColumn(name = "author_id") // Колонка в промежуточной таблице, ссылающаяся на ДРУГУЮ сущность (Author)
    )
    @EqualsAndHashCode.Exclude // Исключаем коллекцию
    @ToString.Exclude // Исключаем коллекцию
    private Set<Author> authors = new HashSet<>(); // Список авторов этой книги

    public void addAuthor(Author author){
        this.authors.add(author);
        if (author.getBooks() != null) { // Проверяем, что коллекция инициализирована
                 author.getBooks().add(this);
             }
    }

    public void removeAuthor(Author author){
        this.authors.remove(author);
         if (author.getBooks() != null) {
              author.getBooks().remove(this);
         }
    }
}
