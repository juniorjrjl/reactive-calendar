package br.com.study.reactivecalendar.api.exceptionprocessor.handler;

import br.com.study.reactivecalendar.api.controller.response.ErrorFieldResponse;
import br.com.study.reactivecalendar.api.controller.response.ProblemResponse;
import br.com.study.reactivecalendar.domain.exception.BeanValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static br.com.study.reactivecalendar.domain.exception.BaseErrorMessage.GENERIC_BAD_REQUEST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Component
public class BeanValidationHandler extends AbstractHandleException<BeanValidationException>{

    private final MessageSource messageSource;

    public BeanValidationHandler(final ObjectMapper mapper, final MessageSource messageSource) {
        super(mapper);
        this.messageSource = messageSource;
    }

    @Override
    public Mono<Void> handlerException(final ServerWebExchange exchange, final BeanValidationException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, BAD_REQUEST);
                    return GENERIC_BAD_REQUEST.getMessage();
                }).map(message -> buildError(BAD_REQUEST, message))
                .flatMap(response -> buildRequestErrorMessage(response, ex))
                .doFirst(() -> log.error("==== BeanValidationException", ex))
                .flatMap(response -> writeResponse(exchange, response));
    }

    private Mono<ProblemResponse> buildRequestErrorMessage(final ProblemResponse response, final BeanValidationException ex){
        return Flux.fromIterable(ex.getBindingResult().getAllErrors())
                .map(objectError -> ErrorFieldResponse.builder()
                        .name(objectError instanceof FieldError fieldError ? fieldError.getField(): objectError.getObjectName())
                        .userMessage(messageSource.getMessage(objectError, LocaleContextHolder.getLocale()))
                        .build())
                .collectList()
                .map(problems -> response.toBuilder().fields(problems).build());
    }

}