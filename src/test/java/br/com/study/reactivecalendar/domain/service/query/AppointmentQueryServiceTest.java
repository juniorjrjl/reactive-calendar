package br.com.study.reactivecalendar.domain.service.query;

import br.com.study.reactivecalendar.core.factoryBot.document.AppointmentDocumentFactoryBot;
import br.com.study.reactivecalendar.domain.exception.NotFoundException;
import br.com.study.reactivecalendar.domain.repository.AppointmentRepository;
import com.github.javafaker.Faker;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.stream.Stream;

import static br.com.study.reactivecalendar.core.RandomData.between;
import static br.com.study.reactivecalendar.core.RandomData.birthday;
import static br.com.study.reactivecalendar.core.RandomData.getFaker;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AppointmentQueryServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    private AppointmentQueryService appointmentQueryService;

    private final Faker faker = getFaker();

    @BeforeEach
    void setup(){
        appointmentQueryService = new AppointmentQueryService(appointmentRepository);
    }

    @Test
    void findByIdTest(){
        var appointment = AppointmentDocumentFactoryBot.builder().build();
        when(appointmentRepository.findById(anyString())).thenReturn(Mono.just(appointment));
        StepVerifier.create(appointmentQueryService.findById(ObjectId.get().toString()))
                .assertNext(actual -> assertThat(actual).isNotNull())
                .verifyComplete();
        verify(appointmentRepository).findById(anyString());
    }

    @Test
    void whenTryToGetNonStoredAppointmentByIdThenThrowError(){
        when(appointmentRepository.findById(anyString())).thenReturn(Mono.empty());
        StepVerifier.create(appointmentQueryService.findById(ObjectId.get().toString()))
                .verifyError(NotFoundException.class);
        verify(appointmentRepository).findById(anyString());
    }

    @Test
    void findAppointmentsWithUserInIntervalTest(){
        var appointments = Stream.generate(() -> AppointmentDocumentFactoryBot.builder().build())
                        .limit(faker.number().randomDigitNotZero())
                                .toList();
        when(appointmentRepository.findUserAppointmentsInInterval(anyString(), any(OffsetDateTime.class), any(OffsetDateTime.class)))
                .thenReturn(Flux.fromIterable(appointments));
        var startIn = birthday();
        var endIn = between(startIn, OffsetDateTime.now().plusYears(20));
        StepVerifier.create(appointmentQueryService.findAppointmentsWithUserInInterval(ObjectId.get().toString(), startIn, endIn))
                .recordWith(ArrayList::new)
                .thenConsumeWhile(actual -> true)
                .consumeRecordedWith(actual -> assertThat(actual).isNotEmpty())
                .verifyComplete();
        verify(appointmentRepository).findUserAppointmentsInInterval(anyString(), any(OffsetDateTime.class), any(OffsetDateTime.class));
    }

}
