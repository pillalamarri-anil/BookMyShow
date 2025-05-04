package dev.anil.bookmyshow.Exceptions;

public class PaymentNotCompletedException extends RuntimeException{
    public PaymentNotCompletedException(String message) {
        super(message);
    }
}
