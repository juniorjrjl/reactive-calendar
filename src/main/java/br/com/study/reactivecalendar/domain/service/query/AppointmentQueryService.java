package br.com.study.reactivecalendar.domain.service.query;

import br.com.study.reactivecalendar.domain.document.AppointmentDocument;
import br.com.study.reactivecalendar.domain.exception.NotFoundException;
import br.com.study.reactivecalendar.domain.repository.AppointmentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

import static br.com.study.reactivecalendar.domain.exception.BaseErrorMessage.APPOINTMENT_NOT_FOUND_EXCEPTION;

@Service
@AllArgsConstructor
@Slf4j
public class AppointmentQueryService {

    private final AppointmentRepository appointmentRepository;

    public Mono<AppointmentDocument> findById(final String id){
        return appointmentRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(APPOINTMENT_NOT_FOUND_EXCEPTION.params(id).getMessage()))))
                .doFirst(() -> log.info("Try to find appointment with id {}", id));
    }

    public Flux<AppointmentDocument> findAppointmentsWithUserInInterval(final String userId,
                                                                        final OffsetDateTime startIn,
                                                                        final OffsetDateTime endIn){
        return appointmentRepository.findUserAppointmentsInInterval(userId, startIn, endIn)
                .doFirst(() -> log.info("==== Checking if user with id {} has another appointment between {} and {}", userId, startIn, endIn));
    }

}
