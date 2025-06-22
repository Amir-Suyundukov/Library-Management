package com.syuyndukov.library.library_managemen.exeption;

/**
 * Когда Пользователь с таким логином/email уже существует
 */
public class UserAlreadyExistsException extends ResourceAlreadyExistsException{

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
    public UserAlreadyExistsException(String userName , String email){
        super("Пользователь с логином " + userName + " или с email " + email + " уже существует");
    }
}
