package br.com.study.reactivecalendar.api.controller.appointment;

import br.com.study.reactivecalendar.api.controller.handler.AppointmentHandler;
import br.com.study.reactivecalendar.api.controller.response.AppointmentSingleResponse;
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
import br.com.study.reactivecalendar.core.factoryBot.request.AppointmentUpdateRequestFactoryBot;
import br.com.study.reactivecalendar.domain.dto.AppointmentDTO;
import br.com.study.reactivecalendar.domain.exception.NotFoundException;
import br.com.study.reactivecalendar.domain.service.AppointmentService;
import br.com.study.reactivecalendar.domain.service.BeanValidationService;
import br.com.study.reactivecalendar.domain.service.query.AppointmentQueryService;
import br.com.study.reactivecalendar.utils.request.RequestBuilder;
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

import static br.com.study.reactivecalendar.utils.request.RequestBuilderInstances.appointmentResponseRequestBuilder;
import static br.com.study.reactivecalendar.utils.request.RequestBuilderInstances.problemResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {AppointmentControllerMapperImpl.class, ApiExceptionHandlerProcessor.class,
        ConflictHandler.class, MethodNotAllowHandler.class, NotFoundHandler.class, ConstraintViolationHandler.class,
        BeanValidationHandler.class, ResponseStatusHandler.class, ReactiveCalenderHandler.class, GenericHandler.class,
        JsonProcessingHandler.class, AppointmentHandler.class, AppointmentRouter.class, BeanValidationService.class})
@WebFluxTest
public class AppointmentHandlerUpdateTest {

    @MockBean
    private AppointmentService appointmentService;
    @MockBean
    private AppointmentQueryService appointmentQueryService;
    @Autowired
    private AppointmentControllerMapper appointmentControllerMapper;
    @Autowired
    private ApplicationContext applicationContext;

    private RequestBuilder<AppointmentSingleResponse> appointmentResponseRequestBuilder;
    private RequestBuilder<ProblemResponse> problemResponseRequestBuilder;

    @BeforeEach
    void setup(){
        appointmentResponseRequestBuilder = appointmentResponseRequestBuilder(applicationContext, "/appointments/");
        problemResponseRequestBuilder = problemResponseRequestBuilder(applicationContext, "/appointments/");
    }

    @Test
    void updateTest(){
        var request = AppointmentUpdateRequestFactoryBot.builder().build();
        var id = ObjectId.get().toString();
        when(appointmentService.update(any(AppointmentDTO.class), any(), any()))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0, AppointmentDTO.class)));
        appointmentResponseRequestBuilder.withUri(uriBuilder -> uriBuilder.pathSegment("{id}").build(id))
                .withBody(request)
                .generateRequestWithSimpleBody()
                .doPut()
                .isHttpStatusIsOk()
                .assertBody(response -> {
                    assertThat(response).usingRecursiveComparison()
                            .ignoringFields("id", "createdAt", "updatedAt", "newGuests", "guestsToRemove", "guests")
                            .isEqualTo(request);
                    assertThat(response.id()).isEqualTo(id);
                });
    }

    @Test
    void whenTryToUpdateNonStoredAppointmentThenReturnBadRequest(){
        var request = AppointmentUpdateRequestFactoryBot.builder().build();
        var id = ObjectId.get().toString();
        when(appointmentService.update(any(AppointmentDTO.class), any(), any())).thenReturn(Mono.error(new NotFoundException("")));
        problemResponseRequestBuilder.withUri(uriBuilder -> uriBuilder.pathSegment("{id}").build(id))
                .withBody(request)
                .generateRequestWithSimpleBody()
                .doPut()
                .isHttpStatusIsNotFound();
    }

    @Test
    void whenRequestHasInvalidDataThenThrowError(){
        var request = AppointmentUpdateRequestFactoryBot.builder().withoutEndIn().build();
        var id = ObjectId.get().toString();
        problemResponseRequestBuilder.withUri(uriBuilder -> uriBuilder.pathSegment("{id}").build(id))
                .withBody(request)
                .generateRequestWithSimpleBody()
                .doPut()
                .isHttpStatusIsBadRequest();
    }

}
