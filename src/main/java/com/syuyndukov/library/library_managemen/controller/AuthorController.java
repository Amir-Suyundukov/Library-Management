package com.syuyndukov.library.library_managemen.controller;

import com.fasterxml.jackson.annotation.OptBoolean;
import com.syuyndukov.library.library_managemen.dto.lib.AuthorCreationDto;
import com.syuyndukov.library.library_managemen.dto.lib.AuthorResponseDto;
import com.syuyndukov.library.library_managemen.dto.lib.AuthorUpdateDto;
import com.syuyndukov.library.library_managemen.service.AuthorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    /**
     * Отображает список всех авторов.
     * Доступно всем аутентифицированным пользователям (согласно ТЗ, просмотр каталога доступен всем ролям).
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String listAuthors(Model model){
        List<AuthorResponseDto> authors = authorService.findAllAuthors();
        model.addAttribute("authors", authors);

        return "author/list";
    }

    /**
     * Отображает форму создания нового автора.
     * Доступно ADMIN и LIBRARIAN.
     */
    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public String showCreateFrom(Model model){
        model.addAttribute("authorForm", new AuthorCreationDto());

        return "author/form";
    }

    /**
     * Обрабатывает отправку формы создания автора.
     * Доступно ADMIN и LIBRARIAN.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public String createAuthor(@ModelAttribute("authorCreationDto") @Valid AuthorCreationDto authorCreationDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model){

        if (bindingResult.hasErrors()){
            System.out.println("Ошибка валидации при создании");
            return "author/form";
        }
        try{
            authorService.createAuthor(authorCreationDto);
            redirectAttributes.addFlashAttribute("successMessage", "Автор успешно создан!");
            System.out.println("Автор создан: " + authorCreationDto.getFullName());
            return "redirect:/authors";

        }catch (RuntimeException e){
            System.err.println("Ошибка при создание пользователя: " + e.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", "Ошибка при создание пользователя: "
            + e.getMessage());
            return "redirect:/authors/new";
        }
    }

    /**
     * Отображает детали автора.
     * Доступно всем аутентифицированным пользователям.
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showAuthor(@PathVariable Long id, Model model){
        Optional<AuthorResponseDto> authorOptional = authorService.findById(id);

        if(authorOptional.isPresent()){
            model.addAttribute("author", authorOptional.get());
            // TODO: Возможно, добавить список книг этого автора
            return "author/details";
        }else {
            // TODO: Обработать случай, когда автор не найден (404 Not Found)
            System.err.println("Автор с ID: " + id + " не найден");
            return "redirect:/authors";
        }
    }

    /**
     * Отображает форму редактирования автора.
     * Доступно ADMIN и LIBRARIAN.
     */
    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public String showEditFrom(@PathVariable Long id, Model model){
        Optional<AuthorResponseDto> authorOptional = authorService.findById(id);

        if (authorOptional.isPresent()){
            AuthorResponseDto author = authorOptional.get();
            // Создаем DTO для обновления и копируем в него данные существующего автора
            // Маппер AuthorMapper сейчас не имеет метода AuthorResponseDto -> AuthorUpdateDto
            // Но мы можем создать AuthorUpdateDto вручную или добавить маппер
            // Для простоты, пока создадим вручную:
            AuthorUpdateDto update = new AuthorUpdateDto();
            update.setFullName(author.getFullName());

            model.addAttribute("authorForm", update);
            model.addAttribute("authorId", id);

            return "author/form";
        }else {
            System.out.println("Автор с ID " + id + " не найден для редактирования.");
            return "redirect:/authors";
        }
    }

    /**
     * Обрабатывает отправку формы редактирования автора (метод POST с URL /update).
     * Доступно ADMIN и LIBRARIAN.
     */
    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public String updateAuthors(@PathVariable Long id, @ModelAttribute("authorUpdateDto") @Valid AuthorUpdateDto authorUpdateDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model){

        if (bindingResult.hasErrors()){
            System.out.println("Ошибки валидации при обновлении автора!");
            model.addAttribute("authorId", id);
            return "author/form";
        }
        try {
            // TODO: Использовать кастомные исключения из сервиса
            authorService.updateAuthor(id,authorUpdateDto);
            redirectAttributes.addFlashAttribute("successMessage", "Автор успешно обновлен");
            System.out.println("Автор обновлен: ID=" + id);

            return "redirect:/authors/" + id;
        }catch (RuntimeException e){ // TODO: Ловить более специфические исключения
            System.err.println("Ошибка при обновлении автора: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при обновлении автора: " + e.getMessage());

            return "redirect:/authors/" + id + "/edit";
        }
    }

    /**
     * Удаляет автора (метод POST с URL /delete).
     * Доступно ADMIN и LIBRARIAN.
     */
    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public String deleteAuthor(@PathVariable Long id, RedirectAttributes redirectAttributes){
        try{
            // TODO: Добавить проверки в сервисе (например, можно ли удалять автора, если у него есть книги?)
            authorService.deleteAuthor(id);
            redirectAttributes.addFlashAttribute("successMessage", "Автор успешно удален!");
            System.out.println("Автор удален: ID=" + id);
        }catch (RuntimeException e){
            System.err.println("Ошибка при удалении автора: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении автора: " + e.getMessage());
        }
        return "redirect:/authors";
    }

    // TODO: Возможно, добавить методы для поиска авторов
}
