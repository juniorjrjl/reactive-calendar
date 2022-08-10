package br.com.study.reactivecalendar.api.exceptionprocessor;

import br.com.study.reactivecalendar.api.exceptionprocessor.handler.ConflictHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.ConstraintViolationHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.GenericHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.JsonProcessingHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.MethodNotAllowHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.NotFoundHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.ReactiveCalenderHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.ResponseStatusHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.WebExchangeBindHandler;
import br.com.study.reactivecalendar.domain.exception.ConflictException;
import br.com.study.reactivecalendar.domain.exception.NotFoundException;
import br.com.study.reactivecalendar.domain.exception.ReactiveCalendarException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;

@Component
@Order(-2)
@Slf4j
@AllArgsConstructor
public class ApiExceptionHandlerProcessor implements WebExceptionHandler {

    private final ConflictHandler conflictHandler;
    private final MethodNotAllowHandler methodNotAllowHandler;
    private final NotFoundHandler notFoundHandler;
    private final ConstraintViolationHandler constraintViolationHandler;
    private final WebExchangeBindHandler webExchangeBindHandler;
    private final ResponseStatusHandler responseStatusHandler;
    private final ReactiveCalenderHandler reactiveCalenderHandler;
    private final GenericHandler genericHandler;
    private final JsonProcessingHandler jsonProcessingHandler;

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable ex) {
        return Mono.error(ex)
                .onErrorResume(ConflictException.class, e -> conflictHandler.handlerException(exchange, e))
                .onErrorResume(MethodNotAllowedException.class, e -> methodNotAllowHandler.handlerException(exchange, e))
                .onErrorResume(NotFoundException.class, e -> notFoundHandler.handlerException(exchange, e))
                .onErrorResume(ConstraintViolationException.class, e -> constraintViolationHandler.handlerException(exchange, e))
                .onErrorResume(WebExchangeBindException.class, e -> webExchangeBindHandler.handlerException(exchange, e))
                .onErrorResume(ResponseStatusException.class, e -> responseStatusHandler.handlerException(exchange, e))
                .onErrorResume(ReactiveCalendarException.class, e -> reactiveCalenderHandler.handlerException(exchange, e))
                .onErrorResume(Exception.class, e -> genericHandler.handlerException(exchange, e))
                .onErrorResume(JsonProcessingException.class, e -> jsonProcessingHandler.handlerException(exchange, e))
                .then();
    }

}
