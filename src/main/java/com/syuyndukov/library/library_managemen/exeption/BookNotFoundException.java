package com.syuyndukov.library.library_managemen.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Когда Книга не найдена
 */
public class BookNotFoundException extends ResourceNotFoundException {

    public BookNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
    public BookNotFoundException(Long bookId){
        super("Книга с ID " + bookId + " не найдена");
    }
    public BookNotFoundException(String isbn){
        super("Книга с ISBN " + isbn + " не найдена");
    }
}
