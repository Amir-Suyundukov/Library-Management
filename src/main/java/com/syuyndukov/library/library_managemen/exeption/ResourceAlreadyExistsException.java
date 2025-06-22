package com.syuyndukov.library.library_managemen.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * исключение, когда ресурс уже существует (например, пользователь с таким логином)
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends RuntimeException{

    public ResourceAlreadyExistsException(String message){
        super(message);
    }

    public ResourceAlreadyExistsException(String message, Throwable cause){
        super(message, cause);
    }
}
