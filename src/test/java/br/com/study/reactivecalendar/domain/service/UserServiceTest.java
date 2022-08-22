package br.com.study.reactivecalendar.domain.service;

import br.com.study.reactivecalendar.core.factoryBot.document.UserDocumentFactoryBot;
import br.com.study.reactivecalendar.domain.document.UserDocument;
import br.com.study.reactivecalendar.domain.exception.NotFoundException;
import br.com.study.reactivecalendar.domain.repository.UserRepository;
import br.com.study.reactivecalendar.domain.service.query.UserQueryService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserQueryService userQueryService;

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setup(){
        userService = new UserService(userQueryService, userRepository);
    }

    @Test
    void saveTest(){
        var user = UserDocumentFactoryBot.builder().preInsert().build();
        when(userRepository.save(any(UserDocument.class))).thenAnswer(invocation -> {
            var document = invocation.getArgument(0, UserDocument.class);
            document = document.toBuilder()
                    .id(ObjectId.get().toString())
                    .createdAt(OffsetDateTime.now())
                    .updatedAt(OffsetDateTime.now())
                    .build();
            return Mono.just(document);
        });
        StepVerifier.create(userService.save(user))
                .assertNext(actual -> {
                    assertThat(actual.id()).isNotNull();
                    assertThat(actual.createdAt()).isNotNull();
                    assertThat(actual.updatedAt()).isNotNull();
                    assertThat(actual).usingRecursiveComparison()
                            .ignoringFields("id", "createdAt", "updatedAt")
                            .isEqualTo(user);
                })
                .verifyComplete();
        verify(userRepository).save(any(UserDocument.class));
    }

    @Test
    void updateTest(){
        var user = UserDocumentFactoryBot.builder().build();
        when(userRepository.save(any(UserDocument.class))).thenAnswer(invocation -> {
            var document = invocation.getArgument(0, UserDocument.class);
            document = document.toBuilder()
                    .updatedAt(OffsetDateTime.now().plusNanos(1L))
                    .build();
            return Mono.just(document);
        });
        when(userQueryService.findById(anyString())).thenReturn(Mono.just(UserDocumentFactoryBot.builder().build()));
        StepVerifier.create(userService.update(user))
                .assertNext(actual -> {
                    assertThat(actual.createdAt().toEpochSecond()).isEqualTo(user.createdAt().toEpochSecond());
                    assertThat(actual.updatedAt()).isAfter(user.updatedAt());
                    assertThat(actual).usingRecursiveComparison()
                            .ignoringFields("createdAt", "updatedAt")
                            .isEqualTo(user);
                })
                .verifyComplete();
        verify(userRepository).save(any(UserDocument.class));
        verify(userQueryService).findById(anyString());
    }

    @Test
    void whenTryToUpdateNonStoredUserThenThrowException(){
        when(userQueryService.findById(anyString())).thenReturn(Mono.error(new NotFoundException("")));
        StepVerifier.create(userService.update(UserDocumentFactoryBot.builder().build()))
                .verifyError(NotFoundException.class);
        verify(userRepository, times(0)).save(any(UserDocument.class));
        verify(userQueryService).findById(anyString());
    }

    @Test
    void deleteTest(){
        when(userQueryService.findById(anyString())).thenReturn(Mono.just(UserDocumentFactoryBot.builder().build()));
        when(userRepository.delete(any(UserDocument.class))).thenReturn(Mono.empty());
        StepVerifier.create(userService.delete(ObjectId.get().toString()))
                .verifyComplete();
        verify(userRepository).delete(any(UserDocument.class));
        verify(userQueryService).findById(anyString());
    }

    @Test
    void whenTryToDeleteNonStoredUserThenThrowException(){
        when(userQueryService.findById(anyString())).thenReturn(Mono.error(new NotFoundException("")));
        StepVerifier.create(userService.delete(ObjectId.get().toString()))
                .verifyError(NotFoundException.class);
        verify(userRepository, times(0)).delete(any(UserDocument.class));
        verify(userQueryService).findById(anyString());
    }

}
