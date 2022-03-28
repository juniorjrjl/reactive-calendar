package br.com.study.reactivecalendar.domain.exception;

public class ReactiveCalendarException extends RuntimeException{

    public ReactiveCalendarException(String message) {
        super(message);
    }

    public ReactiveCalendarException(String message, Throwable cause) {
        super(message, cause);
    }
}
