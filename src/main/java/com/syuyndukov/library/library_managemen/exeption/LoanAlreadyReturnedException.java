package com.syuyndukov.library.library_managemen.exeption;

/**
 * Когда Выдача уже возвращена (при попытке вернуть второй раз)
 */
public class LoanAlreadyReturnedException extends BusinessLogicException{

    public LoanAlreadyReturnedException(String message){
        super(message);
    }
    public LoanAlreadyReturnedException(String message, Throwable cause) {
        super(message, cause);
    }
    public LoanAlreadyReturnedException(Long loanId){
        super("Выдача с ID " + loanId + " уже была возвращена");
    }
}
