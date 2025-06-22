package com.syuyndukov.library.library_managemen.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * исключение, когда операция запрещена по бизнес-логике (например, выдать недоступную книгу)
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessLogicException extends RuntimeException{

    public BusinessLogicException(String message){
    super(message);
    }

    public BusinessLogicException(String message, Throwable cause){
        super(message, cause);
    }
}
