package br.com.study.reactivecalendar.core.validation;

import org.springframework.beans.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Objects;

public class PeriodValidator implements ConstraintValidator<Period, Object> {

    private boolean allowEndNull;

    @Override
    public void initialize(final Period constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try{
            var startPropertyName = getFieldName(value, Period.StartDate.class);
            var startDate = getPeriodFieldValue(value, startPropertyName);
            var allowStartNull = Arrays.stream(value.getClass().getDeclaredFields()).filter(f -> f.isAnnotationPresent(Period.StartDate.class))
                    .map(AccessibleObject::getAnnotations)
                    .map(a -> Arrays.stream(a).filter(ann -> ann.annotationType().equals(Period.StartDate.class)).findFirst())
                    .map(a -> ((Period.StartDate) a.get()).allowNull()).findFirst().get();
            var endPropertyName = getFieldName(value, Period.EndDate.class);
            var endDate = getPeriodFieldValue(value, endPropertyName);
            var allowEndNull = Arrays.stream(value.getClass().getDeclaredFields()).filter(f -> f.isAnnotationPresent(Period.EndDate.class))
                    .map(AccessibleObject::getAnnotations)
                    .map(a -> Arrays.stream(a).filter(ann -> ann.annotationType().equals(Period.EndDate.class)).findFirst())
                    .map(a -> ((Period.EndDate) a.get()).allowNull()).findFirst().get();
            if (Objects.isNull(startDate) && !allowStartNull){
                return false;
            }
            if (Objects.isNull(endDate) && !allowEndNull){
                return false;
            }
            if (Objects.nonNull(startDate) && Objects.nonNull(endDate)){
                return endDate.isAfter(startDate);
            }
        } catch (Exception ex){
            throw new ValidationException(ex);
        }
        return false;
    }

    private String getFieldName(final Object value, final Class<? extends Annotation> clazz){
        return Arrays.stream(value.getClass().getDeclaredFields()).filter(f -> f.isAnnotationPresent(clazz))
                .findFirst().get().getName();
    }

    private OffsetDateTime getPeriodFieldValue(final Object value, final String fieldName) throws InvocationTargetException, IllegalAccessException {
        return (OffsetDateTime) BeanUtils.getPropertyDescriptor(value.getClass(), fieldName).getReadMethod()
                .invoke(value);
    }


}
