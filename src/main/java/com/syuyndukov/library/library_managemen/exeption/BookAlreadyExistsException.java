package com.syuyndukov.library.library_managemen.exeption;

/**
 * Когда Книга с таким ISBN уже существует
 */
public class BookAlreadyExistsException extends ResourceAlreadyExistsException{

    public BookAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
    public BookAlreadyExistsException(String isbn){
        super("Книга с ISBN " + isbn + " уже существует");
    }
}
