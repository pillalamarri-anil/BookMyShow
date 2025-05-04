package dev.anil.bookmyshow.Exceptions;

public class SeatNotAvailableException extends RuntimeException{
    public SeatNotAvailableException(String message) {
        super(message);
    }

}
