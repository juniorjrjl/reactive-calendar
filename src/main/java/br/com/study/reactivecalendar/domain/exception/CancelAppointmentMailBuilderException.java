package br.com.study.reactivecalendar.domain.exception;

public class CancelAppointmentMailBuilderException extends ReactiveCalendarException{

    public CancelAppointmentMailBuilderException(final String message) {
        super(message);
    }

    public CancelAppointmentMailBuilderException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
