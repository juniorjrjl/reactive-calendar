package br.com.study.reactivecalendar.domain.exception;

public class ReactiveCalendarException extends RuntimeException{

    public ReactiveCalendarException(final String message) {
        super(message);
    }

    public ReactiveCalendarException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
