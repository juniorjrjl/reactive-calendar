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
import br.com.study.reactivecalendar.core.factoryBot.request.AppointmentRequestFactoryBot;
import br.com.study.reactivecalendar.domain.dto.AppointmentDTO;
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
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

import static br.com.study.reactivecalendar.utils.request.RequestBuilderInstances.appointmentResponseRequestBuilder;
import static br.com.study.reactivecalendar.utils.request.RequestBuilderInstances.problemResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {AppointmentControllerMapperImpl.class, ApiExceptionHandlerProcessor.class,
        ConflictHandler.class, MethodNotAllowHandler.class, NotFoundHandler.class, ConstraintViolationHandler.class,
        BeanValidationHandler.class, ResponseStatusHandler.class, ReactiveCalenderHandler.class, GenericHandler.class,
        JsonProcessingHandler.class, AppointmentHandler.class, AppointmentRouter.class, BeanValidationService.class})
@WebFluxTest
public class AppointmentHandlerSaveTest {

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
    void saveTest(){
        var request = AppointmentRequestFactoryBot.builder().build();
        when(appointmentService.save(any(AppointmentDTO.class))).thenAnswer(invocation -> {
            var document = invocation.getArgument(0, AppointmentDTO.class);
            document = document.toBuilder()
                    .id(ObjectId.get().toString())
                    .createdAt(OffsetDateTime.now())
                    .updatedAt(OffsetDateTime.now())
                    .build();
            return Mono.just(document);
        });
        appointmentResponseRequestBuilder.withUri(UriBuilder::build)
                .withBody(request)
                .generateRequestWithSimpleBody()
                .doPost()
                .isHttpStatusIsCreated()
                .assertBody(response -> assertThat(response).usingRecursiveComparison()
                            .ignoringFields("id", "createdAt", "updatedAt")
                            .isEqualTo(request));
        verify(appointmentService).save(any(AppointmentDTO.class));
    }

    @Test
    void whenRequestHasInvalidDataThenThrowError(){
        var request = AppointmentRequestFactoryBot.builder().withoutGuests().build();
        problemResponseRequestBuilder.withUri(UriBuilder::build)
                .withBody(request)
                .generateRequestWithSimpleBody()
                .doPost()
                .isHttpStatusIsBadRequest();
        verify(appointmentService, times(0)).save(any(AppointmentDTO.class));
    }

}
