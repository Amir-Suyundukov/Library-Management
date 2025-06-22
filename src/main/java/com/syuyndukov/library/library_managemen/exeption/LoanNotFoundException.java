package com.syuyndukov.library.library_managemen.exeption;

/**
 * Когда Запись о выдаче (Loan) не найдена
 */
public class LoanNotFoundException extends ResourceNotFoundException{

    public LoanNotFoundException(String message) {
        super(message);
    }
    public LoanNotFoundException(String message , Throwable cause){
        super(message, cause);
    }
    public LoanNotFoundException(Long loanId){
        super("Запись о выдачи с ID " + loanId + " не найдена");
    }
}
