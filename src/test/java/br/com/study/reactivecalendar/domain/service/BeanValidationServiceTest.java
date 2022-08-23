package br.com.study.reactivecalendar.domain.service;

import br.com.study.reactivecalendar.api.controller.request.AppointmentIdParam;
import br.com.study.reactivecalendar.api.controller.request.AppointmentRequest;
import br.com.study.reactivecalendar.api.controller.request.AppointmentUpdateRequest;
import br.com.study.reactivecalendar.api.controller.request.UserIdParam;
import br.com.study.reactivecalendar.api.controller.request.UserRequest;
import br.com.study.reactivecalendar.core.factoryBot.request.AppointmentIdParamFactoryBot;
import br.com.study.reactivecalendar.core.factoryBot.request.AppointmentRequestFactoryBot;
import br.com.study.reactivecalendar.core.factoryBot.request.AppointmentUpdateRequestFactoryBot;
import br.com.study.reactivecalendar.core.factoryBot.request.UserIdParamFactoryBot;
import br.com.study.reactivecalendar.core.factoryBot.request.UserRequestFactoryBot;
import br.com.study.reactivecalendar.domain.exception.BeanValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import reactor.test.StepVerifier;

import java.util.stream.Stream;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LocalValidatorFactoryBean.class)
public class BeanValidationServiceTest {

    @Autowired
    private SmartValidator smartValidator;

    private BeanValidationService beanValidationService;

    @BeforeEach
    void setup(){
        beanValidationService = new BeanValidationService(smartValidator);
    }

    private static Stream<Arguments> verifyConstraintsTest(){
        return Stream.of(
                Arguments.of(UserRequestFactoryBot.builder().build(), UserRequest.class.getSimpleName()),
                Arguments.of(UserIdParamFactoryBot.builder().build(), UserIdParam.class.getSimpleName()),
                Arguments.of(AppointmentRequestFactoryBot.builder().build(), AppointmentRequest.class.getSimpleName()),
                Arguments.of(AppointmentUpdateRequestFactoryBot.builder().build(), AppointmentUpdateRequest.class.getSimpleName()),
                Arguments.of(AppointmentIdParamFactoryBot.builder().build(), AppointmentIdParam.class.getSimpleName())
        );
    }

    @ParameterizedTest
    @MethodSource
    void verifyConstraintsTest(final Object target, final String targetName){
        StepVerifier.create(beanValidationService.verifyConstraints(target, targetName)).verifyComplete();
    }

    private static Stream<Arguments> verifyConstraintViolationTest(){
        return Stream.of(
                Arguments.of(UserRequestFactoryBot.builder().withLargeName().build(), UserRequest.class.getSimpleName()),
                Arguments.of(UserRequestFactoryBot.builder().withBlankName().build(), UserRequest.class.getSimpleName()),
                Arguments.of(UserRequestFactoryBot.builder().withBlankEmail().build(), UserRequest.class.getSimpleName()),
                Arguments.of(UserRequestFactoryBot.builder().withInvalidEmail().build(), UserRequest.class.getSimpleName()),
                Arguments.of(UserRequestFactoryBot.builder().withLargeEmail().build(), UserRequest.class.getSimpleName()),
                Arguments.of(UserIdParamFactoryBot.builder().withInvalidId().build(), UserIdParam.class.getSimpleName()),
                Arguments.of(AppointmentRequestFactoryBot.builder().blankTitle().build(), AppointmentRequest.class.getSimpleName()),
                Arguments.of(AppointmentRequestFactoryBot.builder().blankDetails().build(), AppointmentRequest.class.getSimpleName()),
                Arguments.of(AppointmentRequestFactoryBot.builder().withoutGuests().build(), AppointmentRequest.class.getSimpleName()),
                Arguments.of(AppointmentRequestFactoryBot.builder().withoutGuestAdmin().build(), AppointmentRequest.class.getSimpleName()),
                Arguments.of(AppointmentRequestFactoryBot.builder().withoutStartIn().build(), AppointmentRequest.class.getSimpleName()),
                Arguments.of(AppointmentRequestFactoryBot.builder().withoutEndIn().build(), AppointmentRequest.class.getSimpleName()),
                Arguments.of(AppointmentRequestFactoryBot.builder().invalidPeriod().build(), AppointmentRequest.class.getSimpleName()),
                Arguments.of(AppointmentUpdateRequestFactoryBot.builder().blankTitle().build(), AppointmentUpdateRequest.class.getSimpleName()),
                Arguments.of(AppointmentUpdateRequestFactoryBot.builder().blankDetails().build(), AppointmentUpdateRequest.class.getSimpleName()),
                Arguments.of(AppointmentUpdateRequestFactoryBot.builder().withoutStartIn().build(), AppointmentUpdateRequest.class.getSimpleName()),
                Arguments.of(AppointmentUpdateRequestFactoryBot.builder().withoutEndIn().build(), AppointmentUpdateRequest.class.getSimpleName()),
                Arguments.of(AppointmentUpdateRequestFactoryBot.builder().invalidPeriod().build(), AppointmentUpdateRequest.class.getSimpleName()),
                Arguments.of(AppointmentUpdateRequestFactoryBot.builder().invalidEmailInGuestToRemove().build(), AppointmentUpdateRequest.class.getSimpleName()),
                Arguments.of(AppointmentIdParamFactoryBot.builder().withInvalidId().build(), AppointmentIdParam.class.getSimpleName())
        );
    }

    @ParameterizedTest
    @MethodSource
    void verifyConstraintViolationTest(final Object target, final String targetName){
        StepVerifier.create(beanValidationService.verifyConstraints(target, targetName))
                .verifyError(BeanValidationException.class);
    }

}
