package br.com.study.reactivecalendar.api.controller;

import br.com.study.reactivecalendar.api.controller.documentation.AppointmentControllerDocumentation;
import br.com.study.reactivecalendar.api.controller.request.AppointmentRequest;
import br.com.study.reactivecalendar.api.controller.response.AppointmentSingleResponse;
import br.com.study.reactivecalendar.api.mapper.AppointmentControllerMapper;
import br.com.study.reactivecalendar.domain.service.AppointmentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("/appointments")
@Slf4j
@AllArgsConstructor
public class AppointmentController implements AppointmentControllerDocumentation {

    private final AppointmentControllerMapper appointmentControllerMapper;
    private final AppointmentService appointmentService;

    @Override
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<AppointmentSingleResponse> save(final AppointmentRequest request) {
        return appointmentService.save(appointmentControllerMapper.toDTO(request))
                .map(appointmentControllerMapper::toResponse);
    }

}
