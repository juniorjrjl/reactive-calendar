package br.com.study.reactivecalendar.core.validation;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MongoIdValidator implements ConstraintValidator<MongoId, String> {

    @Override
    public void initialize(final MongoId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return StringUtils.isNotBlank(value) && ObjectId.isValid(value);
    }

}
