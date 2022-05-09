package br.com.study.reactivecalendar.domain.exception;

public class RetryException extends ReactiveCalendarException{

    public RetryException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
