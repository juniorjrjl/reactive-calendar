package br.com.study.reactivecalendar.domain.exception;

public class MailBuilderException extends ReactiveCalendarException{

    public MailBuilderException(final String message) {
        super(message);
    }

    public MailBuilderException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
