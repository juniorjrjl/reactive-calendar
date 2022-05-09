package br.com.study.reactivecalendar.domain.helper;

import br.com.study.reactivecalendar.domain.exception.RetryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.function.Predicate;

import static br.com.study.reactivecalendar.domain.exception.BaseErrorMessage.GENERIC_MAX_RETRIES;

@Component
@Slf4j
public class RetryHelper {

    private final Integer maxRetries;
    private final Integer minDuration;

    public RetryHelper(@Value("${retryable.max-retries}") final Integer maxRetries,
                       @Value("${retryable.min-duration}") final Integer minDuration) {
        this.maxRetries = maxRetries;
        this.minDuration = minDuration;
    }

    public Retry processRetry(final String retryIdentifier, final Predicate<? super Throwable> errorFilter){
        return Retry.backoff(maxRetries, Duration.ofSeconds(minDuration))
                .filter(errorFilter)
                .doBeforeRetry(retrySignal -> log.warn("Retrying {} - {} time(s)", retryIdentifier, retrySignal.totalRetries()))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                        new RetryException(GENERIC_MAX_RETRIES.getMessage(), retrySignal.failure()));
    }

}
