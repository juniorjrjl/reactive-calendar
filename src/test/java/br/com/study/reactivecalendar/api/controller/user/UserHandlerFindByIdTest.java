package br.com.study.reactivecalendar.api.controller.user;

import br.com.study.reactivecalendar.api.controller.handler.UserHandler;
import br.com.study.reactivecalendar.api.controller.response.ErrorFieldResponse;
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
import br.com.study.reactivecalendar.core.factoryBot.document.UserDocumentFactoryBot;
import br.com.study.reactivecalendar.domain.exception.NotFoundException;
import br.com.study.reactivecalendar.domain.service.BeanValidationService;
import br.com.study.reactivecalendar.domain.service.UserService;
import br.com.study.reactivecalendar.domain.service.query.UserQueryService;
import br.com.study.reactivecalendar.utils.request.RequestBuilder;
import com.github.javafaker.Faker;
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
import reactor.core.publisher.Mono;

import static br.com.study.reactivecalendar.core.RandomData.getFaker;
import static br.com.study.reactivecalendar.utils.request.RequestBuilderInstances.problemResponseRequestBuilder;
import static br.com.study.reactivecalendar.utils.request.RequestBuilderInstances.userResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {UserMapperImpl.class, ApiExceptionHandlerProcessor.class,
        ConflictHandler.class, MethodNotAllowHandler.class, NotFoundHandler.class, ConstraintViolationHandler.class,
        BeanValidationHandler.class, ResponseStatusHandler.class, ReactiveCalenderHandler.class, GenericHandler.class,
        JsonProcessingHandler.class, UserHandler.class, UserRouter.class, BeanValidationService.class})
@WebFluxTest
public class UserHandlerFindByIdTest {

    @MockBean
    private UserService userService;
    @MockBean
    private UserQueryService userQueryService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ApplicationContext applicationContext;

    private final static Faker faker = getFaker();

    private RequestBuilder<UserSingleResponse> userResponseRequestBuilder;
    private RequestBuilder<ProblemResponse> problemResponseRequestBuilder;

    @BeforeEach
    void setup(){
        userResponseRequestBuilder = userResponseRequestBuilder(applicationContext, "/users/");
        problemResponseRequestBuilder = problemResponseRequestBuilder(applicationContext, "/users/");
    }

    @Test
    void findByIdTest(){
        var document = UserDocumentFactoryBot.builder().build();
        when(userQueryService.findById(anyString())).thenReturn(Mono.just(document));
        userResponseRequestBuilder.withUri(uriBuilder -> uriBuilder.pathSegment("{id}").build(ObjectId.get().toString()))
                .generateRequestWithSimpleBody()
                .doGet()
                .isHttpStatusIsOk()
                .assertBody(response ->assertThat(response).usingRecursiveComparison()
                            .ignoringFields( "createdAt", "updatedAt")
                            .isEqualTo(document));
    }

    @Test
    void whenUseInvalidIdThenReturnBadRequest(){
        problemResponseRequestBuilder.withUri(uriBuilder -> uriBuilder.pathSegment("{id}").build(faker.lorem().word()))
                .generateRequestWithSimpleBody()
                .doGet()
                .isHttpStatusIsBadRequest()
                .assertBody(response -> assertThat(response.fields().stream().map(ErrorFieldResponse::name)).contains("id"));
    }

    @Test
    void whenTryFindNonStoredUserThenReturnNotFound(){
        when(userQueryService.findById(anyString())).thenReturn(Mono.error(new NotFoundException("")));
        problemResponseRequestBuilder.withUri(uriBuilder -> uriBuilder.pathSegment("{id}").build(ObjectId.get().toString()))
                .generateRequestWithSimpleBody()
                .doGet()
                .isHttpStatusIsNotFound();
    }

}
