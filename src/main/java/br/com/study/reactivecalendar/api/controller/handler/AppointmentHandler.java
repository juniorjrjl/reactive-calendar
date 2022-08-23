package br.com.study.reactivecalendar.api.controller.handler;

import br.com.study.reactivecalendar.api.controller.request.AppointmentIdParam;
import br.com.study.reactivecalendar.api.controller.request.AppointmentRequest;
import br.com.study.reactivecalendar.api.controller.request.AppointmentUpdateRequest;
import br.com.study.reactivecalendar.api.controller.response.AppointmentSingleResponse;
import br.com.study.reactivecalendar.api.controller.response.UserSingleResponse;
import br.com.study.reactivecalendar.api.mapper.AppointmentControllerMapper;
import br.com.study.reactivecalendar.domain.service.AppointmentService;
import br.com.study.reactivecalendar.domain.service.BeanValidationService;
import br.com.study.reactivecalendar.domain.service.query.AppointmentQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@AllArgsConstructor
@Slf4j
public class AppointmentHandler {

    private final BeanValidationService beanValidationService;
    private final AppointmentControllerMapper appointmentControllerMapper;
    private final AppointmentService appointmentService;
    private final AppointmentQueryService appointmentQueryService;

    public Mono<ServerResponse> findById(final ServerRequest request){
        return getIdParam(request)
                .flatMap(appointmentQueryService::findById)
                .map(appointmentControllerMapper::toResponse)
                .flatMap(response -> ok()
                        .contentType(APPLICATION_JSON)
                        .body(Mono.just(response), UserSingleResponse.class));
    }

    public Mono<ServerResponse> save(final ServerRequest request){
        return request.bodyToMono(AppointmentRequest.class)
                .flatMap(appointment -> beanValidationService.verifyConstraints(appointment, AppointmentRequest.class.getSimpleName())
                        .thenReturn(appointment))
                .flatMap(appointment -> appointmentService.save(appointmentControllerMapper.toDTO(appointment)))
                .map(appointmentControllerMapper::toResponse)
                .flatMap(response -> created(UriComponentsBuilder.fromPath("/appointments")
                        .pathSegment("{id}")
                        .build(response.id()))
                        .contentType(APPLICATION_JSON)
                        .body(response, UserSingleResponse.class));
    }

    public Mono<ServerResponse> update(final ServerRequest request){
        return request.bodyToMono(AppointmentUpdateRequest.class)
                .flatMap(appointment -> beanValidationService.verifyConstraints(appointment, AppointmentUpdateRequest.class.getSimpleName())
                        .thenReturn(appointment))
                .zipWhen(appointment -> getIdParam(request))
                .flatMap(tuple -> appointmentService.update(appointmentControllerMapper.toDTO(tuple.getT1(), tuple.getT2()),
                        appointmentControllerMapper.toGuestsDTO(tuple.getT1().newGuests()), tuple.getT1().guestsToRemove()))
                .map(appointmentControllerMapper::toResponse)
                .flatMap(response -> ok()
                        .contentType(APPLICATION_JSON)
                        .body(Mono.just(response), AppointmentSingleResponse.class));
    }

    public Mono<ServerResponse> delete(final ServerRequest request){
        return getIdParam(request)
                .flatMap(appointmentService::delete)
                .then(noContent().build());
    }

    private Mono<String> getIdParam(final ServerRequest request){
        return Mono.just(new AppointmentIdParam(request.pathVariable("id")))
                .flatMap(param -> beanValidationService.verifyConstraints(param, AppointmentIdParam.class.getSimpleName()))
                .thenReturn(request.pathVariable("id"));
    }

}
