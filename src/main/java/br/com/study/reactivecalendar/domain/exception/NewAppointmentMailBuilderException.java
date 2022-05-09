package br.com.study.reactivecalendar.domain.exception;

public class NewAppointmentMailBuilderException extends ReactiveCalendarException{

    public NewAppointmentMailBuilderException(final String message) {
        super(message);
    }

    public NewAppointmentMailBuilderException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
