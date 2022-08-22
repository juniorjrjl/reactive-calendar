package br.com.study.reactivecalendar.domain.service;

import br.com.study.reactivecalendar.core.factoryBot.document.UserDocumentFactoryBot;
import br.com.study.reactivecalendar.domain.exception.NotFoundException;
import br.com.study.reactivecalendar.domain.repository.UserRepository;
import br.com.study.reactivecalendar.domain.service.query.UserQueryService;
import com.github.javafaker.Faker;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static br.com.study.reactivecalendar.core.RandomData.getFaker;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserQueryServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserQueryService userQueryService;

    private final Faker faker = getFaker();

    @BeforeEach
    void setup(){
        userQueryService = new UserQueryService(userRepository);
    }

    @Test
    void findByIdTest(){
        var user = UserDocumentFactoryBot.builder().build();
        when(userRepository.findById(anyString())).thenReturn(Mono.just(user));
        StepVerifier.create(userQueryService.findById(ObjectId.get().toString()))
                .assertNext(actual -> assertThat(actual).isNotNull())
                .verifyComplete();
        verify(userRepository).findById(anyString());
    }

    @Test
    void whenTryToGetNonStoredUserByIdThenThrowError(){
        when(userRepository.findById(anyString())).thenReturn(Mono.empty());
        StepVerifier.create(userQueryService.findById(ObjectId.get().toString()))
                .verifyError(NotFoundException.class);
        verify(userRepository).findById(anyString());
    }

    @Test
    void findByEmailTest(){
        var user = UserDocumentFactoryBot.builder().build();
        when(userRepository.findFirstByEmail(anyString())).thenReturn(Mono.just(user));
        StepVerifier.create(userQueryService.findByEmail(faker.internet().emailAddress()))
                .assertNext(actual -> assertThat(actual).isNotNull())
                .verifyComplete();
        verify(userRepository).findFirstByEmail(anyString());
    }

    @Test
    void whenTryToGetNonStoredUserByEmailThenThrowError(){
        when(userRepository.findFirstByEmail(anyString())).thenReturn(Mono.empty());
        StepVerifier.create(userQueryService.findByEmail(ObjectId.get().toString()))
                .verifyError(NotFoundException.class);
        verify(userRepository).findFirstByEmail(anyString());
    }

}
