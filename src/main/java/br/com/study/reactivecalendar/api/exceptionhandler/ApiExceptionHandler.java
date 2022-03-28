package br.com.study.reactivecalendar.api.exceptionhandler;

import br.com.study.reactivecalendar.domain.exception.ConflictException;
import br.com.study.reactivecalendar.domain.exception.InvalidValueException;
import br.com.study.reactivecalendar.domain.exception.NotFoundException;
import br.com.study.reactivecalendar.domain.exception.ReactiveCalendarException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import javax.validation.ConstraintViolationException;
import java.rmi.RemoteException;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@RestControllerAdvice
@Slf4j
@AllArgsConstructor
public class ApiExceptionHandler {

    private final MessageSource messageSource;

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemResponse handleConstraintViolationException(final ConstraintViolationException ex){
        log.error("=== ConstraintViolationException", ex);
        final var errorFields = ex.getConstraintViolations()
                .parallelStream()
                .map(fieldError -> ErrorFieldResponse.builder()
                        .name(((PathImpl) fieldError.getPropertyPath()).getLeafNode().toString())
                        .userMessage(fieldError.getMessage())
                        .build()).toList();
        return buildError(ProblemResponse.builder(), BAD_REQUEST, "")
                .fields(errorFields)
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public ProblemResponse handleWebExchangeBindException(final WebExchangeBindException ex){
        log.error("=== WebExchangeBindException", ex);
        final var errorFields = ex.getAllErrors()
                .parallelStream()
                .map(objectError -> {
                    var fieldError = objectError.getObjectName();
                    if (objectError instanceof FieldError){
                        fieldError = ((FieldError)objectError).getField();
                    }
                    return ErrorFieldResponse.builder()
                            .name(fieldError)
                            .userMessage(messageSource.getMessage(objectError, LocaleContextHolder.getLocale()))
                            .build();
                }).collect(Collectors.toList());
        var errorMessage = messageSource.getMessage("generic.invalidParameter", null, LocaleContextHolder.getLocale());
        return buildError(ProblemResponse.builder(), BAD_REQUEST, errorMessage)
                .fields(errorFields)
                .build();
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ProblemResponse handleGenericException(final Exception ex) {
        log.error("=== Exception", ex);
        var errorMessage = messageSource.getMessage("generic", null, LocaleContextHolder.getLocale());
        return buildError(ProblemResponse.builder(), INTERNAL_SERVER_ERROR, errorMessage).build();
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ReactiveCalendarException.class)
    public ProblemResponse handleGenericException(final ReactiveCalendarException ex) {
        log.error("=== Exception", ex);
        var errorMessage = messageSource.getMessage("generic", null, LocaleContextHolder.getLocale());
        return buildError(ProblemResponse.builder(), INTERNAL_SERVER_ERROR, errorMessage).build();
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ProblemResponse handleNotFoundException(final NotFoundException ex) {
        log.error("=== NotFoundException", ex);
        return buildError(ProblemResponse.builder(), NOT_FOUND, ex.getMessage()).build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(InvalidValueException.class)
    public ProblemResponse handleInvalidValueException(final InvalidValueException ex) {
        log.error("=== InvalidValueException", ex);
        return buildError(ProblemResponse.builder(), BAD_REQUEST, ex.getMessage()).build();
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ProblemResponse handleConflictException(final ConflictException ex) {
        log.error("=== ConflictException", ex);
        return buildError(ProblemResponse.builder(), SERVICE_UNAVAILABLE, ex.getMessage()).build();
    }

    @ResponseStatus(SERVICE_UNAVAILABLE)
    @ExceptionHandler(RemoteException.class)
    public ProblemResponse handleRemoteException(final RemoteException ex) {
        log.error("=== RemoteException", ex);
        return buildError(ProblemResponse.builder(), SERVICE_UNAVAILABLE, ex.getMessage()).build();
    }

    private ProblemResponse.ProblemResponseBuilder buildError(final ProblemResponse.ProblemResponseBuilder errorBuilder,
                                                              final HttpStatus status, final String errorDescription){
        return errorBuilder.timestamp(OffsetDateTime.now())
                .status(status.value())
                .errorDescription(errorDescription);
    }

}
