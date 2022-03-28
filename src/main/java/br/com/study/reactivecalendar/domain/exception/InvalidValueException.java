package br.com.study.reactivecalendar.domain.exception;

public class InvalidValueException extends ReactiveCalendarException{

    public InvalidValueException(final String message) {
        super(message);
    }

    public InvalidValueException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
