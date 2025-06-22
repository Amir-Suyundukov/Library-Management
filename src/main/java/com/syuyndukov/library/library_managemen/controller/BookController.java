package com.syuyndukov.library.library_managemen.controller;

import com.syuyndukov.library.library_managemen.dto.lib.*;
import com.syuyndukov.library.library_managemen.service.AuthorService;
import com.syuyndukov.library.library_managemen.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;


    /**
     * Отображает форму создания новой книги.
     * Доступно ADMIN и LIBRARIAN.
     */
    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public String showCreateFrom(Model model){
        model.addAttribute("bookForm", new BookCreationDto());

        List<AuthorResponseDto> allAuthors = authorService.findAllAuthors();
        model.addAttribute("allAuthors", allAuthors);

        return "book/form";
    }

    /**
     * Отображает детали конкретной книги.
     * Доступно всем, кто может видеть каталог (permitAll).
     */
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public String showBook(@PathVariable Long id, Model model){
        Optional<BookResponseDto> bookOptional = bookService.findById(id);

        if (bookOptional.isPresent()){
            model.addAttribute("book", bookOptional.get());
            // TODO: Возможно, добавить список выдач этой книги
            return "book/details";
        }else {
            System.err.println("Книга с ID " + id + " не найдена.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Книга с ID " + id + " не найдена");
        }
    }

    /**
     * Отображает форму редактирования книги.
     * Доступно ADMIN и LIBRARIAN.
     */
    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public String showEditForm(@PathVariable Long id, Model model){
        Optional<BookResponseDto> bookResponseDto = bookService.findById(id);

        if(bookResponseDto.isPresent()){
            BookResponseDto book = bookResponseDto.get();

            BookUpdateDto bookUpdateDto = new BookUpdateDto();
            bookUpdateDto.setTitle(book.getTitle());
            bookUpdateDto.setPublicationYear(book.getPublicationYear());
            bookUpdateDto.setCopies(book.getCopies());
            bookUpdateDto.setIsbn(book.getIsbn());

            List<AuthorResponseDto> allAuthors = authorService.findAllAuthors();
            model.addAttribute("allAuthors", allAuthors);

            Set<Long> authorsiIds = new HashSet<>();
            if(book.getAuthors() != null){
                authorsiIds = book.getAuthors().stream().map(AuthorResponseDto::getId).collect(Collectors.toSet());
            }
            bookUpdateDto.setAuthorIds(authorsiIds);

            model.addAttribute("bookForm", bookUpdateDto);
            model.addAttribute("bookId", id);

            return "book/form";
        }else {
            System.err.println("Книга с ID " + id + " не найдена для редактирования.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Книга с ID " + id + " не найдена");
        }
    }

    /**
     * Обрабатывает отправку формы создания книги.
     * Доступно ADMIN и LIBRARIAN.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public String createBook(@ModelAttribute ("bookForm") @Valid BookCreationDto bookCreationDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model){
        // TODO: Проверка на уникальность ISBN в сервисе или здесь

        if(bindingResult.hasErrors()){
            System.out.println("Ошибка валидации при создание книг!");
            List<AuthorResponseDto> list = authorService.findAllAuthors();
            model.addAttribute("allAuthors", list);

            return "book/form";
        }try {
            bookService.createBook(bookCreationDto);
            redirectAttributes.addFlashAttribute("successMessage", "Книга успешно создана!");
            System.out.println("Книга создана: " + bookCreationDto.getTitle());

            return "redirect:/books";

        }catch (RuntimeException e) {
            System.err.println("Ошибка при создании книги: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "ошибка при создание книги: " +
                    e.getMessage());
            List<AuthorResponseDto> list = authorService.findAllAuthors();
            model.addAttribute("allAuthors", list);

            return "book/list";
        }
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public String updateBook(@PathVariable Long id, @ModelAttribute("bookForm") @Valid BookUpdateDto bookUpdateDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        if (bindingResult.hasErrors()) {
            System.out.println("Ошибки валидации при обновлении книги!");
            model.addAttribute("bookId", id);

            List<AuthorResponseDto> allAuthors = authorService.findAllAuthors();
            model.addAttribute("allAuthors", allAuthors);

            return "book/form";
        }
        try {
            bookService.updateBook(id, bookUpdateDto);
            redirectAttributes.addFlashAttribute("successMessage", "Книга успешно обновлена!");
            System.out.println("Книга обновлена: ID=" + id);
            return "redirect:/books/" + id;
        } catch (RuntimeException e) { // TODO: Ловить более специфические исключения
            System.err.println("Ошибка при обновлении книги: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при обновлении книги: " + e.getMessage());
            return "redirect:/books/" + id + "/edit";
        }
    }

    /**
     * Удаляет книгу.
     * Доступно ADMIN и LIBRARIAN.
     */
    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes){
        try{
            bookService.deleteBook(id);
            redirectAttributes.addFlashAttribute("successMessage", "Книга успешно удалена!");
            System.out.println("Книга удалена: ID=" + id);

        }catch (RuntimeException e){
            System.err.println("Ошибка при удалении книги ID=" + id + ": " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении книги: " + e.getMessage());
        }
        return "redirect:/books";
    }

    /**
     * Отображает каталог книг ИЛИ результаты поиска.
     * Принимает необязательный параметр запроса 'query' для поиска.
     * Доступно всем (permitAll).
     */
    @GetMapping
    @PreAuthorize("permitAll()")
    public String listBooks(@RequestParam(value = "query", required = false) String query, Model model){
        List<BookResponseDto> books;
        if (query != null && !query.trim().isEmpty()){
            books = bookService.searchBooks(query);
            model.addAttribute("searchQuery", query);
            System.out.println("Выполнен поиск по запросу: " + query);
        }else {
            books = bookService.findAllBooks();
            System.out.println("Отображен полный каталог. Так как нет совпадений");
        }
        model.addAttribute("books", books);

        return "book/list";
    }
}
