package br.com.study.reactivecalendar.api.controller;

import br.com.study.reactivecalendar.api.controller.documentation.AppointmentControllerDocumentation;
import br.com.study.reactivecalendar.api.controller.request.AppointmentRequest;
import br.com.study.reactivecalendar.api.controller.request.AppointmentUpdateRequest;
import br.com.study.reactivecalendar.api.controller.response.AppointmentFindResponse;
import br.com.study.reactivecalendar.api.controller.response.AppointmentSingleResponse;
import br.com.study.reactivecalendar.api.mapper.AppointmentControllerMapper;
import br.com.study.reactivecalendar.core.validation.MongoId;
import br.com.study.reactivecalendar.domain.service.AppointmentService;
import br.com.study.reactivecalendar.domain.service.query.AppointmentQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("/appointments")
@Slf4j
@AllArgsConstructor
public class AppointmentController implements AppointmentControllerDocumentation {

    private final AppointmentControllerMapper appointmentControllerMapper;
    private final AppointmentService appointmentService;
    private final AppointmentQueryService appointmentQueryService;

    @Override
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<AppointmentSingleResponse> save(final AppointmentRequest request) {
        return appointmentService.save(appointmentControllerMapper.toDTO(request))
                .map(appointmentControllerMapper::toResponse);
    }

    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Mono<AppointmentSingleResponse> update(@PathVariable @Valid
                                                      @MongoId(message = "{appointmentController.id}")final String id,
                                                  @RequestBody @Valid final AppointmentUpdateRequest request){
        return appointmentService.update(appointmentControllerMapper.toDTO(request, id),
                        appointmentControllerMapper.toGuestsDTO(request.newGuests()),
                        request.guestsToRemove())
                .map(appointmentControllerMapper::toResponse);
    }

    @Override
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Mono<AppointmentFindResponse> findById(@PathVariable @Valid
                                                        @MongoId(message = "{appointmentController.id}")final String id){
        return appointmentQueryService.findById(id)
                .map(appointmentControllerMapper::toResponse);
    }


    @DeleteMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> delete(@PathVariable @Valid @MongoId(message = "{appointmentController.id}") final String id){
        return appointmentService.delete(id)
                .doFirst(() -> log.info("try to delete a appointment with follow id {}", id));
    }

}
