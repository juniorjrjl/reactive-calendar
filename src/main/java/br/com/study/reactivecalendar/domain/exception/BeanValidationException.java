package br.com.study.reactivecalendar.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;

@AllArgsConstructor
@Getter
public class BeanValidationException extends RuntimeException{

    private final BindingResult bindingResult;

}
