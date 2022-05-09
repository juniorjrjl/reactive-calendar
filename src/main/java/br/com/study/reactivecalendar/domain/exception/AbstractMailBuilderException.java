package br.com.study.reactivecalendar.domain.exception;

public class AbstractMailBuilderException extends ReactiveCalendarException{

    public AbstractMailBuilderException(final String message) {
        super(message);
    }

    public AbstractMailBuilderException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
