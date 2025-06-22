package com.syuyndukov.library.library_managemen.exeption;

/**
 *  Когда Пользователь не найден
 */
public class UserNotFoundException extends ResourceNotFoundException{

    public UserNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
    public UserNotFoundException(Long userId){
        super("Пользователь с ID: " + userId + " не найден");// удобный конструктор по ID
    }
    public UserNotFoundException(String username) {
        super("Пользователь с логином '" + username + "' не найден"); // удобный конструктор по ID
    }
}
