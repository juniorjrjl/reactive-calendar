package br.com.study.reactivecalendar.core.validation;

import br.com.study.reactivecalendar.api.controller.request.GuestRequest;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

import static br.com.study.reactivecalendar.domain.document.GuestType.ADMIN;

public class AppointmentWithAdminValidator implements ConstraintValidator<AppointmentWithAdmin, Set<GuestRequest>> {

    @Override
    public void initialize(final AppointmentWithAdmin constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final Set<GuestRequest> value, final ConstraintValidatorContext context) {
        return CollectionUtils.isNotEmpty(value) && value.stream().anyMatch(g -> g.type().equals(ADMIN));
    }
}
