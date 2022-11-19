package br.com.study.reactivecalendar.api.controller.appointment;

import br.com.study.reactivecalendar.api.controller.handler.AppointmentHandler;
import br.com.study.reactivecalendar.api.controller.response.ErrorFieldResponse;
import br.com.study.reactivecalendar.api.controller.response.ProblemResponse;
import br.com.study.reactivecalendar.api.controller.router.AppointmentRouter;
import br.com.study.reactivecalendar.api.exceptionprocessor.ApiExceptionHandlerProcessor;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.BeanValidationHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.ConflictHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.ConstraintViolationHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.GenericHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.JsonProcessingHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.MethodNotAllowHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.NotFoundHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.ReactiveCalenderHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.ResponseStatusHandler;
import br.com.study.reactivecalendar.api.mapper.AppointmentControllerMapper;
import br.com.study.reactivecalendar.api.mapper.AppointmentControllerMapperImpl;
import br.com.study.reactivecalendar.core.factoryBot.document.AppointmentDocumentFactoryBot;
import br.com.study.reactivecalendar.domain.exception.NotFoundException;
import br.com.study.reactivecalendar.domain.service.AppointmentService;
import br.com.study.reactivecalendar.domain.service.BeanValidationService;
import br.com.study.reactivecalendar.domain.service.query.AppointmentQueryService;
import br.com.study.reactivecalendar.utils.request.RequestBuilder;
import com.github.javafaker.Faker;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import static br.com.study.reactivecalendar.core.RandomData.getFaker;
import static br.com.study.reactivecalendar.utils.request.RequestBuilderInstances.noBodyResponseRequestBuilder;
import static br.com.study.reactivecalendar.utils.request.RequestBuilderInstances.problemResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {AppointmentControllerMapperImpl.class, ApiExceptionHandlerProcessor.class,
        ConflictHandler.class, MethodNotAllowHandler.class, NotFoundHandler.class, ConstraintViolationHandler.class,
        BeanValidationHandler.class, ResponseStatusHandler.class, ReactiveCalenderHandler.class, GenericHandler.class,
        JsonProcessingHandler.class, AppointmentHandler.class, AppointmentRouter.class, BeanValidationService.class})
@WebFluxTest
public class AppointmentHandlerDeleteTest {

    @MockBean
    private AppointmentService appointmentService;
    @MockBean
    private AppointmentQueryService appointmentQueryService;
    @Autowired
    private AppointmentControllerMapper appointmentControllerMapper;;
    @Autowired
    private ApplicationContext applicationContext;

    private final static Faker faker = getFaker();

    private RequestBuilder<Void> emptyBodyResponseRequestBuilder;
    private RequestBuilder<ProblemResponse> problemResponseRequestBuilder;

    @BeforeEach
    void setup(){
        emptyBodyResponseRequestBuilder = noBodyResponseRequestBuilder(applicationContext, "/appointments/");
        problemResponseRequestBuilder = problemResponseRequestBuilder(applicationContext, "/appointments/");
    }

    @Test
    void deleteTest(){
        var document = AppointmentDocumentFactoryBot.builder().build();
        when(appointmentService.delete(anyString())).thenReturn(Mono.empty());
        emptyBodyResponseRequestBuilder.withUri(uriBuilder -> uriBuilder.pathSegment("{id}").build(ObjectId.get().toString()))
                .generateRequestWithoutBody()
                .doDelete()
                .isHttpStatusIsNoContent();
    }

    @Test
    void whenUseInvalidIdThenReturnBadRequest(){
        problemResponseRequestBuilder.withUri(uriBuilder -> uriBuilder.pathSegment("{id}").build(faker.lorem().word()))
                .generateRequestWithSimpleBody()
                .doDelete()
                .isHttpStatusIsBadRequest()
                .assertBody(response -> assertThat(response.fields().stream().map(ErrorFieldResponse::name)).contains("id"));
    }

    @Test
    void whenTryFindNonStoredAppointmentThenReturnNotFound(){
        when(appointmentService.delete(anyString())).thenReturn(Mono.error(new NotFoundException("")));
        problemResponseRequestBuilder.withUri(uriBuilder -> uriBuilder.pathSegment("{id}").build(ObjectId.get().toString()))
                .generateRequestWithSimpleBody()
                .doDelete()
                .isHttpStatusIsNotFound();
    }

}
