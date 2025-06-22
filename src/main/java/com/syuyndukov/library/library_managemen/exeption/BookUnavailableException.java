package com.syuyndukov.library.library_managemen.exeption;

/**
 * Когда Книга недоступна для выдачи (нет экземпляров)
 */
public class BookUnavailableException extends BusinessLogicException{

    public BookUnavailableException(String message) {
        super(message);
    }
    public BookUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
    public BookUnavailableException(String bookTitle, Integer bookId) {
        super("Книга '" + bookTitle + "' (ID=" + bookId + ") недоступна для выдачи (нет свободных экземпляров).");
    }
}
