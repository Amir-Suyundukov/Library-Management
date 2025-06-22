package com.syuyndukov.library.library_managemen.advice;

import com.syuyndukov.library.library_managemen.exeption.BookNotFoundException;
import com.syuyndukov.library.library_managemen.exeption.BusinessLogicException;
import com.syuyndukov.library.library_managemen.exeption.ResourceAlreadyExistsException;
import com.syuyndukov.library.library_managemen.exeption.ResourceNotFoundException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключения ResourceNotFoundException (404 Not Found).
     * Перенаправляет на страницу 404 или отображает сообщение об ошибке.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlerResourceNotFoundException(ResourceNotFoundException ex, Model model,
                                                   HttpServletRequest request) {

        System.err.println("Resource Not Found Error: " + ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());

        return "error";
    }

    /**
     * Обрабатывает исключения ResourceAlreadyExistsException (409 Conflict).
     * Перенаправляет на страницу ошибки или отображает сообщение.
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex, Model model) {

        System.err.println("Conflict Error: " + ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());

        return "error";
    }

    /**
     * Обрабатывает исключения BusinessLogicException (400 Bad Request).
     * Перенаправляет на страницу ошибки или отображает сообщение.
     */
    @ExceptionHandler(BusinessLogicException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBusinessLogicException(BusinessLogicException ex, Model model) {

        System.err.println("Business Logic Error: " + ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());

        return "error";
    }

//    @ExceptionHandler(BookNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String handleBookNotFoundException(BookNotFoundException ex, Model model) {
//        model.addAttribute("errorMessage", ex.getMessage());
//        return "error";
//    }

    /**
     * Обрабатывает исключение handleAllUncaughtException (500Internal Server Error)
     * Перенаправляет на страницу ошибки или отображает сообщение
     * @param ex
     * @param model
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Статус 500 Internal Server Error
    public String handleAllUncaughtException(Exception ex, Model model, HttpServletRequest request) {
        System.err.println("UNEXPECTED ERROR: " + ex.getMessage());
        ex.printStackTrace();
        model.addAttribute("errorMessage", "Произошла непредвиденная ошибка. Пожалуйста, попробуйте позже.");
        model.addAttribute("status", request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
        return "error";
    }
}
