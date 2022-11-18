package br.com.study.reactivecalendar.domain.service;

import br.com.study.reactivecalendar.core.factoryBot.document.AppointmentDocumentFactoryBot;
import br.com.study.reactivecalendar.core.factoryBot.document.UserDocumentFactoryBot;
import br.com.study.reactivecalendar.core.factoryBot.dto.AppointmentDTOFactoryBot;
import br.com.study.reactivecalendar.core.factoryBot.dto.GuestDTOFactoryBot;
import br.com.study.reactivecalendar.domain.document.AppointmentDocument;
import br.com.study.reactivecalendar.domain.document.UserDocument;
import br.com.study.reactivecalendar.domain.dto.AppointmentDTO;
import br.com.study.reactivecalendar.domain.dto.GuestDTO;
import br.com.study.reactivecalendar.domain.dto.MailMessageDTO;
import br.com.study.reactivecalendar.domain.exception.ConflictException;
import br.com.study.reactivecalendar.domain.exception.NotFoundException;
import br.com.study.reactivecalendar.domain.mapper.AppointmentMapper;
import br.com.study.reactivecalendar.domain.mapper.AppointmentMapperImpl;
import br.com.study.reactivecalendar.domain.mapper.GuestMapper;
import br.com.study.reactivecalendar.domain.mapper.GuestMapperImpl;
import br.com.study.reactivecalendar.domain.mapper.MailMapper;
import br.com.study.reactivecalendar.domain.mapper.MailMapperImpl;
import br.com.study.reactivecalendar.domain.mapper.MailMapperImpl_;
import br.com.study.reactivecalendar.domain.repository.AppointmentRepository;
import br.com.study.reactivecalendar.domain.service.query.AppointmentQueryService;
import br.com.study.reactivecalendar.domain.service.query.UserQueryService;
import com.github.javafaker.Faker;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.study.reactivecalendar.core.RandomData.getFaker;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = { GuestMapperImpl.class, MailMapperImpl.class, MailMapperImpl_.class, AppointmentMapperImpl.class})
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private AppointmentQueryService appointmentQueryService;
    @Mock
    private UserQueryService userQueryService;
    @Mock
    private MailService mailService;
    @Autowired
    private GuestMapper guestMapper;
    @Autowired
    private MailMapper mailMapper;
    @Autowired
    private AppointmentMapper appointmentMapper;
    @Captor
    private ArgumentCaptor<MailMessageDTO> mailMessageCaptor;

    private AppointmentService appointmentService;

    private final static Faker faker = getFaker();

    @BeforeEach
    void setup(){
        appointmentService = new AppointmentService(appointmentRepository, appointmentQueryService, userQueryService,
                mailService, guestMapper, mailMapper, appointmentMapper);
    }

    @Test
    void saveTest() throws InterruptedException {
        var appointment = AppointmentDTOFactoryBot.builder().build();
        var users = Stream.generate(() -> UserDocumentFactoryBot.builder().build())
                .limit(appointment.guests().size())
                .toList();
        var usersEmailReplaced = new ArrayList<UserDocument>();
        for (int i = 0; i < users.size(); i++) {
            var guests = new ArrayList<>(appointment.guests());
            usersEmailReplaced.add(users.get(i).toBuilder().email(guests.get(i).email()).build());
        }
        when(userQueryService.findByEmail(anyString())).thenAnswer(invocation -> {
            var email = invocation.getArgument(0, String.class);
            return Mono.defer(() -> Mono.just(usersEmailReplaced.stream().filter(u -> u.email().equals(email)).findFirst().orElseThrow()));
        });
        when(appointmentQueryService.findAppointmentsWithUserInInterval(anyString(), any(OffsetDateTime.class), any(OffsetDateTime.class)))
                .thenReturn(Flux.empty());
        when(appointmentRepository.save(any(AppointmentDocument.class)))
                .thenAnswer(invocation -> {
                    var document = invocation.getArgument(0, AppointmentDocument.class);
                    document = document.toBuilder()
                            .id(ObjectId.get().toString())
                            .createdAt(OffsetDateTime.now())
                            .updatedAt(OffsetDateTime.now())
                            .build();
                    return Mono.just(document);
                });
        when(mailService.send(mailMessageCaptor.capture())).thenReturn(Mono.empty());
        StepVerifier.create(appointmentService.save(appointment))
                .assertNext(actual ->{
                    assertThat(actual).isNotNull();
                })
                        .verifyComplete();
        TimeUnit.SECONDS.sleep(4L);

        var mailMessage = mailMessageCaptor.getValue();
        assertThat(((OffsetDateTime)mailMessage.variables().get("startIn")).toEpochSecond()).isEqualTo(appointment.startIn().toEpochSecond());
        assertThat(((OffsetDateTime)mailMessage.variables().get("endIn")).toEpochSecond()).isEqualTo(appointment.endIn().toEpochSecond());
        assertThat(((String)mailMessage.variables().get("title"))).isEqualTo(appointment.title());
        assertThat(((String)mailMessage.variables().get("details"))).isEqualTo(appointment.details());
        assertThat(mailMessage.destinations()).containsAnyElementsOf(usersEmailReplaced.stream().map(UserDocument::email).toList());
    }

    @Test
    void whenSomeUserHasAnotherAppointmentInSameIntervalThenThrowException(){
        var appointment = AppointmentDTOFactoryBot.builder().build();
        var users = Stream.generate(() -> UserDocumentFactoryBot.builder().build())
                .limit(appointment.guests().size())
                .toList();
        var userCount = new AtomicInteger(0);
        var userAppointmentCount = new AtomicInteger(0);
        var userInteractionToGenerateError = faker.number().numberBetween(0, userCount.get());
        when(userQueryService.findByEmail(anyString())).thenReturn(Mono.defer(() -> Mono.just(users.get(userCount.getAndIncrement()))));
        when(appointmentQueryService.findAppointmentsWithUserInInterval(anyString(), any(OffsetDateTime.class), any(OffsetDateTime.class)))
                .thenReturn(Flux.defer(() -> userInteractionToGenerateError == userAppointmentCount.getAndIncrement() ?
                        Flux.fromIterable(List.of(AppointmentDocumentFactoryBot.builder().build())) :
                        Flux.empty()));
        StepVerifier.create(appointmentService.save(appointment))
                .verifyError(ConflictException.class);
        verify(userQueryService, times(userCount.get())).findByEmail(anyString());
        verify(appointmentQueryService, times(userAppointmentCount.get()))
                .findAppointmentsWithUserInInterval(anyString(), any(OffsetDateTime.class), any(OffsetDateTime.class));
        verify(appointmentRepository, times(0)).save(any(AppointmentDocument.class));
        verify(mailService, times(0)).send(any(MailMessageDTO.class));
    }

    @Test
    void deleteTest() throws InterruptedException {
        var appointment = AppointmentDocumentFactoryBot.builder().build();
        var users = Stream.generate(() -> UserDocumentFactoryBot.builder().build())
                .limit(appointment.guests().size())
                .toList();
        var userCount = new AtomicInteger(0);
        when(appointmentQueryService.findById(anyString())).thenReturn(Mono.just(appointment));
        when(userQueryService.findById(anyString())).thenReturn(Mono.defer(() -> Mono.just(users.get(userCount.getAndIncrement()))));
        when(mailService.send(mailMessageCaptor.capture())).thenReturn(Mono.empty());
        when(appointmentRepository.delete(any(AppointmentDocument.class))).thenReturn(Mono.empty());
        StepVerifier.create(appointmentService.delete(ObjectId.get().toString())).verifyComplete();
        TimeUnit.SECONDS.sleep(2L);
        verify(appointmentQueryService).findById(anyString());
        verify(userQueryService, times(userCount.get())).findById(anyString());
        verify(mailService).send(any(MailMessageDTO.class));
        var mailMessage = mailMessageCaptor.getValue();
        assertThat(((OffsetDateTime)mailMessage.variables().get("startIn")).toEpochSecond()).isEqualTo(appointment.startIn().toEpochSecond());
        assertThat(((OffsetDateTime)mailMessage.variables().get("endIn")).toEpochSecond()).isEqualTo(appointment.endIn().toEpochSecond());
        assertThat(((String)mailMessage.variables().get("title"))).isEqualTo(appointment.title());
        assertThat(((String)mailMessage.variables().get("details"))).isEqualTo(appointment.details());
        assertThat(mailMessage.destinations()).containsAnyElementsOf(users.stream().map(UserDocument::email).toList());
    }

    @Test
    void whenTryToDeleteNonStoredAppointmentThenThrowError(){
        when(appointmentQueryService.findById(anyString())).thenReturn(Mono.error(new NotFoundException("")));
        StepVerifier.create(appointmentService.delete(ObjectId.get().toString()))
                .verifyError(NotFoundException.class);
        verify(appointmentQueryService).findById(anyString());
        verify(userQueryService, times(0)).findById(anyString());
        verify(mailService, times(0)).send(any(MailMessageDTO.class));
    }

    @Test
    void whenTryToUpdateNonStoredAppointmentThenThrowError(){
        when(appointmentQueryService.findById(anyString())).thenReturn(Mono.error(new NotFoundException("")));
        var newGuests = Stream.generate(() -> GuestDTOFactoryBot.builder().build())
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toSet());
        var guestToRemove = Stream.generate(() -> faker.internet().emailAddress())
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toSet());
        StepVerifier.create(appointmentService.update(AppointmentDTOFactoryBot.builder().build(), newGuests, guestToRemove))
                .verifyError(NotFoundException.class);
        verify(userQueryService, times(0)).findById(anyString());
        verify(userQueryService, times(0)).findByEmail(anyString());
        verify(appointmentRepository, times(0)).save(any());
        verify(mailService, times(0)).send(any());
    }

    @Test
    void whenTryPassInvalidUserInAttachedThenThrowError(){
        when(appointmentQueryService.findById(anyString())).thenReturn(Mono.just(AppointmentDocumentFactoryBot.builder().build()));
        when(userQueryService.findById(anyString())).thenReturn(Mono.error(new NotFoundException("")));
        var newGuests = Stream.generate(() -> GuestDTOFactoryBot.builder().build())
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toSet());
        var guestToRemove = Stream.generate(() -> faker.internet().emailAddress())
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toSet());
        StepVerifier.create(appointmentService.update(AppointmentDTOFactoryBot.builder().build(), newGuests, guestToRemove))
                .verifyError(NotFoundException.class);
        verify(userQueryService, times(0)).findByEmail(anyString());
        verify(appointmentRepository, times(0)).save(any());
        verify(mailService, times(0)).send(any());
    }

    @Test
    void whenTryAttachNonStoredUserThenThrowError(){
        when(appointmentQueryService.findById(anyString())).thenReturn(Mono.just(AppointmentDocumentFactoryBot.builder().build()));
        when(userQueryService.findById(anyString())).thenReturn(Mono.just(UserDocumentFactoryBot.builder().build()));
        when(userQueryService.findByEmail(anyString())).thenReturn(Mono.error(new NotFoundException("")));
        var newGuests = Stream.generate(() -> GuestDTOFactoryBot.builder().build())
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toSet());
        var guestToRemove = Stream.generate(() -> faker.internet().emailAddress())
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toSet());
        StepVerifier.create(appointmentService.update(AppointmentDTOFactoryBot.builder().build(), newGuests, guestToRemove))
                .verifyError(NotFoundException.class);
        verify(appointmentRepository, times(0)).save(any());
        verify(mailService, times(0)).send(any());
    }

    private static Stream<Arguments> updateTest(){
        var newGuests = Stream.generate(() -> GuestDTOFactoryBot.builder().build())
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toSet());
        var guestsToRemove = Stream.generate(() -> faker.internet().emailAddress())
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toSet());
        var appointment = AppointmentDTOFactoryBot.builder().build();
        Consumer<List<MailMessageDTO>> newGuestsVerifier = mails ->{
            assertThat(mails.stream().filter(m -> m.template().equals("mail/editInvite")).count()).isOne();
            assertThat(mails.stream().filter(m -> m.template().equals("mail/newInvite")).count()).isOne();
            assertThat(mails.size()).isEqualTo(2);
        };
        Consumer<List<MailMessageDTO>> guestsUpdatedVerifier = mails -> assertThat(mails.size()).isOne();
        Consumer<List<MailMessageDTO>> guestsRemovedVerifier = mails ->{
            assertThat(mails.stream().filter(m -> m.template().equals("mail/removeInvite")).count()).isOne();
            assertThat(mails.stream().filter(m -> m.template().equals("mail/editInvite")).count()).isOne();
            assertThat(mails.size()).isEqualTo(2);
        };
        return Stream.of(
                Arguments.of(appointment, new HashSet<>(), new HashSet<>(), guestsUpdatedVerifier),
                Arguments.of(appointment, newGuests, new HashSet<>(), newGuestsVerifier),
                Arguments.of(appointment, new HashSet<>(), guestsToRemove, guestsRemovedVerifier)
        );
    }

    @MethodSource
    @ParameterizedTest
    void updateTest(final AppointmentDTO dto, final Set<GuestDTO> newGuests, final Set<String> guestsToRemove,
                    final Consumer<List<MailMessageDTO>> mailVerifiers) throws InterruptedException {
        when(appointmentQueryService.findById(anyString())).thenReturn(Mono.just(AppointmentDocumentFactoryBot.builder().build()));
        when(userQueryService.findById(anyString())).thenReturn(Mono.just(UserDocumentFactoryBot.builder().build()));
        if (CollectionUtils.isNotEmpty(newGuests) || CollectionUtils.isNotEmpty(guestsToRemove)) {
            when(userQueryService.findByEmail(anyString())).thenReturn(Mono.just(UserDocumentFactoryBot.builder().build()));
        }
        when(appointmentRepository.save(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0, AppointmentDocument.class)));
        when(mailService.send(mailMessageCaptor.capture())).thenReturn(Mono.empty());
        StepVerifier.create(appointmentService.update(dto, newGuests, guestsToRemove))
                .assertNext(actual -> assertThat(actual).isNotNull())
                .verifyComplete();
        TimeUnit.SECONDS.sleep(2L);
        var mailMessages = mailMessageCaptor.getAllValues();
        mailVerifiers.accept(mailMessages);
    }

}
