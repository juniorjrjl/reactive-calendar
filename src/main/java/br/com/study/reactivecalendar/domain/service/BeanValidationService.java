package br.com.study.reactivecalendar.domain.service;

import br.com.study.reactivecalendar.domain.exception.BeanValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class BeanValidationService {

    private final SmartValidator validator;

    public Mono<Void> verifyConstraints(final Object target, final String targetName){
        return Mono.just(new BeanPropertyBindingResult(target, targetName))
                .map(bindingResult -> generateErrors(target, bindingResult))
                .flatMap(this::validate);

    }

    private BeanPropertyBindingResult generateErrors(final Object target, final BeanPropertyBindingResult bindingResult){
        validator.validate(target, bindingResult);
        return bindingResult;
    }

    private Mono<Void> validate(final BeanPropertyBindingResult bindingResult){
        return Mono.just(bindingResult.hasErrors())
                .filter(BooleanUtils::isFalse)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BeanValidationException(bindingResult))))
                .then();
    }

}
