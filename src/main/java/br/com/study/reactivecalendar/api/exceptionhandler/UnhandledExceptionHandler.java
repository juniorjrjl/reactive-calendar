package br.com.study.reactivecalendar.api.exceptionhandler;

import br.com.study.reactivecalendar.domain.exception.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import static br.com.study.reactivecalendar.domain.exception.BaseErrorMessage.GENERIC_EXCEPTION;
import static br.com.study.reactivecalendar.domain.exception.BaseErrorMessage.GENERIC_METHOD_NOT_ALLOWED_EXCEPTION;
import static br.com.study.reactivecalendar.domain.exception.BaseErrorMessage.GENERIC_NOT_FOUND_EXCEPTION;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Order(-2)
@Slf4j
@AllArgsConstructor
public class UnhandledExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper mapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        return Mono.error(ex)
                .onErrorResume(MethodNotAllowedException.class, e -> handleMethodNotAllowedException(exchange, e))
                .onErrorResume(throwable -> throwable instanceof ResponseStatusException || throwable instanceof NotFoundException, e -> handleResourceNotFoundException(exchange))
                .onErrorResume(e -> handleGenericException(exchange, ex))
                .onErrorResume(JsonProcessingException.class, e -> handleJsonProcessingException(exchange, ex))
                .then();
    }

    private Mono<Void> handleJsonProcessingException(final ServerWebExchange exchange, final Throwable ex) {
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("==== Failed to map exception for the request [{}]", exchange.getRequest().getPath().value(), ex);
        return exchange.getResponse().setComplete();
    }

    private Mono<Void> handleGenericException(final ServerWebExchange exchange, final Throwable ex) {
        return Mono.fromCallable(() ->{
            prepareExchange(exchange, INTERNAL_SERVER_ERROR);
            return GENERIC_EXCEPTION.getMessage();
        }).map(baseErrorMessage -> buildError(INTERNAL_SERVER_ERROR, baseErrorMessage))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse))
                .doFirst(() -> log.error("==== Failed to map exception for the request [{}]", exchange.getRequest().getPath().value(), ex));
    }

    private Mono<?> handleResourceNotFoundException(final ServerWebExchange exchange) {
        return Mono.fromCallable(() ->{
            prepareExchange(exchange, NOT_FOUND);
            return GENERIC_NOT_FOUND_EXCEPTION.getMessage();
        }).map(baseErrorMessage -> this.buildError(NOT_FOUND, baseErrorMessage))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse))
                .doFirst(() -> log.error("==== Has no resource for method [{}] using [{}]", exchange.getRequest().getMethodValue(), exchange.getRequest().getPath().value()));
    }

    private Mono<Void> handleMethodNotAllowedException(final ServerWebExchange exchange, final MethodNotAllowedException ex) {
        return Mono.fromCallable(() ->{
            prepareExchange(exchange, METHOD_NOT_ALLOWED);
            return GENERIC_METHOD_NOT_ALLOWED_EXCEPTION.params(ex.getHttpMethod()).getMessage();
        }).map(baseErrorMessage -> this.buildError(METHOD_NOT_ALLOWED, baseErrorMessage))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse))
                .doFirst(() -> log.error("==== Method [{}] is not allowed at [{}]. Message: {}", exchange.getRequest().getMethodValue(), exchange.getRequest().getPath().value(), ex.getLocalizedMessage()));
    }

    private void prepareExchange(final ServerWebExchange exchange, final HttpStatus statusCoder){
        exchange.getResponse().setStatusCode(statusCoder);
        exchange.getResponse().getHeaders().setContentType(APPLICATION_JSON);
    }

    private Mono<Void> writeResponse(final ServerWebExchange exchange, final ProblemResponse problemResponse) {
        return exchange.getResponse()
                .writeWith(Mono.fromCallable(() -> new DefaultDataBufferFactory().wrap(mapper.writeValueAsBytes(problemResponse))));
    }

    private ProblemResponse buildError(final HttpStatus status, final String baseErrorMessage) {
        return ProblemResponse.builder()
                .status(status.value())
                .errorDescription(baseErrorMessage)
                .build();
    }
}
