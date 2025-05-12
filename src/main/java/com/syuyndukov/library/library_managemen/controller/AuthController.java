package com.syuyndukov.library.library_managemen.controller;

import com.syuyndukov.library.library_managemen.dto.UserCreateDto;
import com.syuyndukov.library.library_managemen.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
@AllArgsConstructor
@Data
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }

    @GetMapping("/")
    public String indexPage(){
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("userCreateDto", new UserCreateDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userCreateDto") @Valid UserCreateDto userCreateDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model){
        if (bindingResult.hasErrors()){
            System.out.println("Ошибка валидации при регистрации!");
            return "register";
        }

        // TODO: Добавить проверку на уникальность username и email перед созданием!
        //  Хотя мы добавили в UserService, можно добавить проверку и здесь, чтобы
        //   в случае ошибки уникальности вернуться на форму с сообщением

        try {
            userService.createUser(userCreateDto);
            redirectAttributes.addFlashAttribute("successMessage", "Регистрация прошла успешно " +
                    "пожалуйста войдите.");
            System.out.println("Пользователь успешно зарегистрирован");
            return "redirect:/login";

        }catch (RuntimeException e){// TODO: Ловить более специфические исключения, например, UserAlreadyExistsException
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при регистрации" + e.getMessage());
            System.err.println("Ошибка при регистрации " + e.getMessage());

            return "redirect:/register";
        }
    }
}
