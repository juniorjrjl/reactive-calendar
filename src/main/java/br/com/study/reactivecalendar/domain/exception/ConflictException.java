package br.com.study.reactivecalendar.domain.exception;

public class ConflictException extends ReactiveCalendarException{

    public ConflictException(final String message) {
        super(message);
    }

    public ConflictException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
