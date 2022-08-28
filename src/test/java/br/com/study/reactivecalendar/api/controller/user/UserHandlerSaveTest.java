package br.com.study.reactivecalendar.api.controller.user;

import br.com.study.reactivecalendar.api.controller.handler.UserHandler;
import br.com.study.reactivecalendar.api.controller.response.ProblemResponse;
import br.com.study.reactivecalendar.api.controller.response.UserSingleResponse;
import br.com.study.reactivecalendar.api.controller.router.UserRouter;
import br.com.study.reactivecalendar.api.exceptionprocessor.ApiExceptionHandlerProcessor;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.BeanValidationHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.ConflictHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.ConstraintViolationHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.GenericHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.JsonProcessingHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.MethodNotAllowHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.NotFoundHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.ReactiveCalenderHandler;
import br.com.study.reactivecalendar.api.exceptionprocessor.handler.ResponseStatusHandler;
import br.com.study.reactivecalendar.api.mapper.UserMapper;
import br.com.study.reactivecalendar.api.mapper.UserMapperImpl;
import br.com.study.reactivecalendar.core.factoryBot.request.UserRequestFactoryBot;
import br.com.study.reactivecalendar.domain.document.UserDocument;
import br.com.study.reactivecalendar.domain.service.BeanValidationService;
import br.com.study.reactivecalendar.domain.service.UserService;
import br.com.study.reactivecalendar.domain.service.query.UserQueryService;
import br.com.study.reactivecalendar.utils.request.RequestBuilder;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

import static br.com.study.reactivecalendar.utils.request.RequestBuilderInstances.problemResponseRequestBuilder;
import static br.com.study.reactivecalendar.utils.request.RequestBuilderInstances.userResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {UserMapperImpl.class, ApiExceptionHandlerProcessor.class,
        ConflictHandler.class, MethodNotAllowHandler.class, NotFoundHandler.class, ConstraintViolationHandler.class,
        BeanValidationHandler.class, ResponseStatusHandler.class, ReactiveCalenderHandler.class, GenericHandler.class,
        JsonProcessingHandler.class, UserHandler.class, UserRouter.class, BeanValidationService.class})
@WebFluxTest
public class UserHandlerSaveTest {

    @MockBean
    private UserService userService;
    @MockBean
    private UserQueryService userQueryService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ApplicationContext applicationContext;

    private RequestBuilder<UserSingleResponse> userResponseRequestBuilder;
    private RequestBuilder<ProblemResponse> problemResponseRequestBuilder;

    @BeforeEach
    void setup(){
        userResponseRequestBuilder = userResponseRequestBuilder(applicationContext, "/users/");
        problemResponseRequestBuilder = problemResponseRequestBuilder(applicationContext, "/users/");
    }

    @Test
    void saveTes(){
        var request = UserRequestFactoryBot.builder().build();
        when(userService.save(any(UserDocument.class))).thenAnswer(invocation -> {
            var document = invocation.getArgument(0, UserDocument.class);
            document = document.toBuilder()
                    .id(ObjectId.get().toString())
                    .createdAt(OffsetDateTime.now())
                    .updatedAt(OffsetDateTime.now())
                    .build();
            return Mono.just(document);
        });
        userResponseRequestBuilder.withUri(UriBuilder::build)
                .withBody(request)
                .generateRequestWithSimpleBody()
                .doPost()
                .isHttpStatusIsCreated()
                .assertBody(response ->{
                    assertThat(response).usingRecursiveComparison()
                            .ignoringFields("id", "createdAt", "updatedAt")
                            .isEqualTo(request);
                });
        verify(userService).save(any(UserDocument.class));
    }

    @Test
    void whenRequestHasInvalidDataThenThrowError(){
        var request = UserRequestFactoryBot.builder().withLargeEmail().build();
        problemResponseRequestBuilder.withUri(UriBuilder::build)
                .withBody(request)
                .generateRequestWithSimpleBody()
                .doPost()
                .isHttpStatusIsBadRequest();
        verify(userService, times(0)).save(any(UserDocument.class));
    }

}
