package com.syuyndukov.library.library_managemen.exeption;

/**
 * Когда не указан хотя бы один автор при создании/обновлении книги
 */
public class BookMustHaveAuthorException extends BusinessLogicException{

    public BookMustHaveAuthorException(String message, Throwable cause) {
        super(message, cause);
    }
    public BookMustHaveAuthorException(){
        super("Книга должна иметь хотя бы одного автора");
    }

}
