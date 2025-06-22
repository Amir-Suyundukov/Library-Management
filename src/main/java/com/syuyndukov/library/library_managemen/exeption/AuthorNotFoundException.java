package com.syuyndukov.library.library_managemen.exeption;

/**
 * Когда Автор не найден
 */
public class AuthorNotFoundException extends ResourceNotFoundException{

    public AuthorNotFoundException(String message , Throwable cause){
        super(message, cause);
    }
    public AuthorNotFoundException(Long authorId){
        super("Автор с ID " + authorId + " не найден");
    }
    public AuthorNotFoundException(String fullName){
        super("Автор с именем " + fullName + " не найден");
    }
}
