package br.com.study.reactivecalendar.domain.service;

import br.com.study.reactivecalendar.domain.AppointmentPreUpdate;
import br.com.study.reactivecalendar.domain.document.AppointmentDocument;
import br.com.study.reactivecalendar.domain.document.UserDocument;
import br.com.study.reactivecalendar.domain.dto.AppointmentDTO;
import br.com.study.reactivecalendar.domain.dto.GuestDTO;
import br.com.study.reactivecalendar.domain.dto.MailMessageDTO;
import br.com.study.reactivecalendar.domain.dto.mailbuilder.MailMessageDTOBuilder;
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
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                .flatMap(appointment -> notify(MailMessageDTO.buildNewAppointment(), appointment, appointment.guests().stream().map(GuestDTO::email).collect(Collectors.toSet()))
                        .thenReturn(appointment));
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

    public Mono<Void> delete(final String id){
        return appointmentQueryService.findById(id)
                .map(appointmentMapper::toDTO)
                .flatMap(dto -> setEmails(dto.guests())
                        .collectList()
                        .map(guests -> dto.toBuilder().guests(Set.copyOf(guests)).build()))
                .flatMap(appointment -> notify(MailMessageDTO.buildCancelAppointment(), appointment, appointment.guests().stream().map(GuestDTO::email).collect(Collectors.toSet()))
                        .thenReturn(appointment))
                .map(appointmentMapper::toDocument)
                .flatMap(appointmentRepository::delete)
                .doFirst(() -> log.info("try to delete appointment with id {}", id));
    }

    public Flux<GuestDTO> setEmails(final Set<GuestDTO> dto){
        return Flux.fromIterable(dto)
                .flatMap(guest -> userQueryService.findById(guest.userId())
                        .map(user -> appointmentMapper.setEmail(guest, user)));
    }

    public Mono<AppointmentDTO> update(final AppointmentDTO dto, final Set<GuestDTO> newGuests, final Set<String> guestsToRemove){
        return appointmentQueryService.findById(dto.id())
                .flatMapIterable(AppointmentDocument::guests)
                .flatMap(guest -> userQueryService.findById(guest.userId())
                        .map(user -> appointmentMapper.toDTO(user, guest.type())))
                .collectList()
                .zipWhen(d -> buildNewGuests(newGuests))
                .map(t -> Stream.concat(t.getT1().stream(), t.getT2().stream()).toList())
                .map(guests -> dto.toBuilder().guests(Set.copyOf(guests)).build())
                .flatMap(appointmentDTO -> buildPreUpdateDTO(appointmentDTO, newGuests, guestsToRemove))
                .flatMap(appointmentPreUpdate -> notify(MailMessageDTO.buildRemoveAppointment(), appointmentPreUpdate.appointmentDTO(), appointmentPreUpdate.getGuestsEmailToRemove())
                        .thenReturn(appointmentPreUpdate))
                .flatMap(appointmentPreUpdate -> notify(MailMessageDTO.buildNewAppointment(), appointmentPreUpdate.appointmentDTO(), appointmentPreUpdate.getNewGuestsEmail())
                        .thenReturn(appointmentPreUpdate))
                .flatMap(appointmentPreUpdate -> notify(MailMessageDTO.buildEditAppointment(), appointmentPreUpdate.appointmentDTO(), appointmentPreUpdate.getGuestsAlreadyInAppointment())
                        .thenReturn(appointmentPreUpdate.appointmentDTO()));
    }


    private Mono<List<GuestDTO>> buildNewGuests(final Set<GuestDTO> guests){
        return Flux.fromIterable(guests)
                .flatMap(guest -> userQueryService.findByEmail(guest.email())
                        .map(user -> appointmentMapper.toDTO(user, guest.type())))
                .collectList();

    }

    private Mono<Void> verifyNewGuests(final List<String> emails){
        return Flux.fromIterable(emails)
                .flatMap(userQueryService::findByEmail)
                .then();
    }

    private Mono<AppointmentPreUpdate> buildPreUpdateDTO(final AppointmentDTO dto, Set<GuestDTO> newGuests, final Set<String> guestsToRemove){
        return Mono.just(AppointmentPreUpdate.builder().appointmentDTO(dto).newGuest(newGuests))
                .flatMap(builder -> getGuestToRemove(guestsToRemove)
                        .map(users -> builder.guestsToRemove(Set.copyOf(users)).build()))
                .flatMap(this::updateGuests);

    }

    private Mono<List<UserDocument>> getGuestToRemove(final Set<String> guestsToRemove){
        return Flux.defer(() -> CollectionUtils.isEmpty(guestsToRemove) ?
                Flux.empty() :
                Flux.fromIterable(guestsToRemove)
                        .flatMap(userQueryService::findByEmail))
                .collectList();
    }

    private Mono<AppointmentPreUpdate> updateGuests(final AppointmentPreUpdate dto){
        return removeGuests(dto)
                .map(d -> d.toBuilder().addNewGuests().build())
                .flatMap(appointmentPreUpdate -> {
                    var document = appointmentMapper.toDocument(appointmentPreUpdate.appointmentDTO());
                    return appointmentRepository.save(document).thenReturn(appointmentPreUpdate);
                });
    }

    private Mono<AppointmentPreUpdate> removeGuests(final AppointmentPreUpdate dto){
        return Flux.fromIterable(dto.appointmentDTO().guests())
                .filter(guest -> dto.guestsToRemove().stream().map(UserDocument::email).noneMatch(email -> email.equals(guest.email())))
                .collectList()
                .map(guests -> dto.toBuilder().appointmentDTO(dto.appointmentDTO().toBuilder().guests(Set.copyOf(guests)).build()).build());
    }

    private Mono<Void> notify(final MailMessageDTOBuilder builder, final AppointmentDTO dto, final Set<String> destinations){
        return Mono.just(CollectionUtils.isEmpty(destinations))
                .filter(BooleanUtils::isTrue)
                .switchIfEmpty(Mono.defer(() -> Mono.fromCallable(() -> mailMapper.toMailMessageDTO(builder, dto, destinations.toArray(String[]::new)))
                        .onTerminateDetach()
                        .doOnSuccess(mailMessage -> mailService.send(mailMessage).subscribe())
                        .doOnNext(mailMessage -> log.info("==== sending mail from owners {}", mailMessage.destinations()))
                        .thenReturn(true)))
                .then();
    }


}
