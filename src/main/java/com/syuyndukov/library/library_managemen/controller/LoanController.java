package com.syuyndukov.library.library_managemen.controller;

import com.syuyndukov.library.library_managemen.domain.User;
import com.syuyndukov.library.library_managemen.domain.loan.Loan;
import com.syuyndukov.library.library_managemen.dto.UserResponseDto;
import com.syuyndukov.library.library_managemen.dto.lib.BookResponseDto;
import com.syuyndukov.library.library_managemen.dto.loan.LoanCreationDto;
import com.syuyndukov.library.library_managemen.dto.loan.LoanResponseDto;
import com.syuyndukov.library.library_managemen.service.BookService;
import com.syuyndukov.library.library_managemen.service.LoanService;
import com.syuyndukov.library.library_managemen.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.Authenticator;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;
    private final BookService bookService;
    private final UserService userService;

    /**
     * Отображает список всех активных выдач (книги на руках).
     * Доступно только Библиотекарю.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public String listActiveLoans(Model model){

        List<LoanResponseDto> activeLoans = loanService.findActiveLoans();

        model.addAttribute("activeLoans" , activeLoans);

        return "loan/active-list";
    }

    /**
     * Отображает форму для выдачи новой книги.
     * Доступно только Библиотекарю.
     * В модель передаются списки всех Книг и всех Пользователей для выбора.
     */
    @GetMapping("/lend")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public String showLendFrom(Model model){

        model.addAttribute("loanForm", new LoanCreationDto());

        List<BookResponseDto> allBooks = bookService.findAllBooks();
        List<UserResponseDto> allUser = userService.findAllUsers();

        model.addAttribute("allBooks", allBooks);
        model.addAttribute("allUsers", allUser);

        return "loan/lend-form";
    }

    /**
     * Обрабатывает отправку формы выдачи книги.
     * Доступно только Библиотекарю.
     * Вызывает сервис для оформления выдачи.
     */
    @PostMapping("/lend")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public String processLendForm(@ModelAttribute("loanForm") @Valid LoanCreationDto loanCreationDto,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes,
                                  Model model){

        if (bindingResult.hasErrors()) {
            System.out.println("Ошибки валидации при оформлении выдачи!");
            // Если ошибки, возвращаемся на форму.
            // Важно! Снова передаем списки всех книг и пользователей в модель!
            List<BookResponseDto> allBooks = bookService.findAllBooks();
            List<UserResponseDto> allUsers = userService.findAllUsers();
            model.addAttribute("allBooks", allBooks);
            model.addAttribute("allUsers", allUsers);

            return "loan/lend-form";
        }
        try {
            loanService.lendBook(loanCreationDto);

            redirectAttributes.addFlashAttribute("successMessage", "Выдача успешно оформлена!");
            System.out.println("Выдача оформлена: Книга ID=" + loanCreationDto.getBookId() + ", Пользователь ID=" + loanCreationDto.getUserId());

            return "redirect:/loans";

        } catch (RuntimeException e){

            System.err.println("Ошибка при оформлении выдачи: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при оформлении выдачи: " + e.getMessage());

            // В случае ошибки, возвращаемся на форму выдачи с сообщением об ошибке
            // Важно! Снова передаем списки всех книг и пользователей в модель!
            List<BookResponseDto> allBooks = bookService.findAllBooks();
            List<UserResponseDto> allUsers = userService.findAllUsers();
            model.addAttribute("allBooks", allBooks);
            model.addAttribute("allUsers", allUsers);
            // Spring сам добавит "loanForm" и BindingResult обратно в модель.

            return "loan/lend-form";
        }
    }

    /**
     * Обрабатывает оформление возврата книги по записи о выдаче.
     * Доступно только Библиотекарю.
     * Вызывает сервис для оформления возврата.
     */
    @PostMapping("/return/{id}")
    @PreAuthorize("isAuthenticated()")
    public String processReturn(@PathVariable Long id, RedirectAttributes redirectAttributes){
        try {
            loanService.returnBook(id);
            redirectAttributes.addFlashAttribute("successMessage", "Возврат успешно оформлен!");
            System.out.println("Возврат оформлен для выдачи ID: '" + id);
        } catch (RuntimeException e){
            System.err.println("Ошибка при оформлении возврата для выдачи ID=" + id + ": " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при оформлении возврата: " + e.getMessage());
        }

        return "redirect:/loans";
    }

    /**
     * Отображает список активных выдач для текущего залогиненного пользователя ("Мои Книги").
     * Доступно только аутентифицированным пользователям (роль READER и выше).
     */
    @GetMapping("/my-books")
    @PreAuthorize("isAuthenticated()")
    public String myLoans(Model model , Authentication authentication){
        if (authentication != null && authentication.isAuthenticated()){

            User currentUser = (User) authentication.getPrincipal();
            Long currentUserId = currentUser.getId();

            List<LoanResponseDto> myActiveLoans = loanService.findActiveLoansByUserId(currentUserId);

            model.addAttribute("myActiveLoans", myActiveLoans);
            model.addAttribute("currentUser", currentUser);

            return "user/my-loans";

        }else {

            return "redirect:/login";
        }
    }

    /**
     * Отображает детали конкретной записи о выдаче.
     * Доступно только Библиотекарю (и, возможно, Админу).
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public String ShowLoanDetails(@PathVariable Long id, Model model){

        Optional<LoanResponseDto> loanOptional = loanService.findById(id);

        if (loanOptional.isPresent()){

            model.addAttribute("loan", loanOptional.get());

            return "loan/details";

        }else {
            System.err.println("Запись о выдаче с ID " + id + " не найдена.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запись о выдаче с ID " + id + " не найдена");
        }
    }
}
