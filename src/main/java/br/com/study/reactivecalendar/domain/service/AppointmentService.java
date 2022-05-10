package br.com.study.reactivecalendar.domain.service;

import br.com.study.reactivecalendar.domain.document.UserDocument;
import br.com.study.reactivecalendar.domain.dto.AppointmentDTO;
import br.com.study.reactivecalendar.domain.dto.GuestDTO;
import br.com.study.reactivecalendar.domain.exception.ConflictException;
import br.com.study.reactivecalendar.domain.mapper.AppointmentMapper;
import br.com.study.reactivecalendar.domain.mapper.GuestMapper;
import br.com.study.reactivecalendar.domain.mapper.MailMapper;
import br.com.study.reactivecalendar.domain.repository.AppointmentRepository;
import br.com.study.reactivecalendar.domain.service.query.AppointmentQueryService;
import br.com.study.reactivecalendar.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

import static br.com.study.reactivecalendar.domain.exception.BaseErrorMessage.USER_ALREADY_HAS_APPOINTMENT_IN_INTERVAL;

@Service
@Slf4j
@AllArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentQueryService appointmentQueryService;
    private final UserQueryService userQueryService;
    private final MailService mailService;
    private final GuestMapper guestMapper;
    private final MailMapper mailMapper;
    private final AppointmentMapper appointmentMapper;

    public Mono<AppointmentDTO> save(final AppointmentDTO dto){
        return Flux.fromIterable(dto.guests())
                .flatMap(g -> userQueryService.findByEmail(g.email())
                        .flatMap(user -> userAlreadyHasAppointmentInInterval(user, dto.startIn(), dto.endIn())))
                .collectList()
                .map(u -> dto.toBuilder().guests(guestMapper.toDTOSet(u, dto.guests())).build())
                .flatMap(appointmentDTO -> appointmentRepository.save(appointmentMapper.toDocument(appointmentDTO))
                        .map(document -> appointmentDTO.toBuilder().id(document.id()).build()))
                .flatMap(this::notifyNewAppointment);
    }

    private Mono<UserDocument> userAlreadyHasAppointmentInInterval(final UserDocument user,
                                                                   final OffsetDateTime startIn,
                                                                   final OffsetDateTime endIn){
        return appointmentQueryService.findAppointmentsWithUserInInterval(user.id(), startIn, endIn)
                .collectList()
                .filter(CollectionUtils::isEmpty)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ConflictException(USER_ALREADY_HAS_APPOINTMENT_IN_INTERVAL
                        .params(user.email())
                        .getMessage()))))
                .map(appointments -> user);
    }

    private Mono<AppointmentDTO> notifyNewAppointment(final AppointmentDTO dto){
        return Mono.just(mailMapper.toNewAppointmentMailMessage(dto))
                .onTerminateDetach()
                .doOnSuccess(mailMessage -> mailService.send(mailMessage).subscribe())
                .doFirst(() -> log.info("==== sending mail from owners {}", dto.guests().stream().map(GuestDTO::email).collect(Collectors.joining(","))))
                .thenReturn(dto);
    }

    public Mono<Void> delete(final String id){
        return appointmentQueryService.findById(id)
                .flatMap(appointmentRepository::delete)
                .doFirst(() -> log.info("try to delete appointment with id {}", id));
    }

}
