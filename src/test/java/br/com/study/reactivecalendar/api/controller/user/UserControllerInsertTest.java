package br.com.study.reactivecalendar.api.controller.user;

import br.com.study.reactivecalendar.ReactiveCalendarApplication;
import br.com.study.reactivecalendar.api.controller.UserController;
import br.com.study.reactivecalendar.api.controller.request.UserRequest;
import br.com.study.reactivecalendar.api.controller.response.UserSingleResponse;
import br.com.study.reactivecalendar.api.exceptionhandler.ErrorFieldResponse;
import br.com.study.reactivecalendar.api.exceptionhandler.ProblemResponse;
import br.com.study.reactivecalendar.api.mapper.UserMapperImpl;
import br.com.study.reactivecalendar.core.EmbeddedMongoDbConfig;
import br.com.study.reactivecalendar.core.factoryBot.request.UserRequestFactoryBot;
import br.com.study.reactivecalendar.domain.repository.UserRepository;
import br.com.study.reactivecalendar.domain.service.UserService;
import br.com.study.reactivecalendar.domain.service.query.UserQueryService;
import br.com.study.reactivecalendar.utils.request.RequestBuilder;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriBuilder;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.study.reactivecalendar.utils.request.RequestBuilderInstances.problemResponseRequestBuilder;
import static br.com.study.reactivecalendar.utils.request.RequestBuilderInstances.userResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ContextConfiguration(classes = {ReactiveCalendarApplication.class, UserMapperImpl.class, UserQueryService.class,
        UserService.class, UserRepository.class, EmbeddedMongoDbConfig.class})
@WebFluxTest({UserController.class, UserRepository.class})
@AutoConfigureDataMongo
@ExtendWith({SpringExtension.class})
public class UserControllerInsertTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    private RequestBuilder<UserSingleResponse> requestBuilder;
    private RequestBuilder<ProblemResponse> requestBuilderError;

    @BeforeEach
    void setup(){
        requestBuilder = userResponseRequestBuilder(applicationContext, "/users");
        requestBuilderError = problemResponseRequestBuilder(applicationContext, "/users");
    }

    @Test
    void saveTest(){
        var request = UserRequestFactoryBot.builder().build();
        requestBuilder.withUri(UriBuilder::build)
                .withBody(request)
                .generateRequestWithSimpleBody()
                .doPost()
                .isHttpStatusIsCreated()
                .assertBody(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response).hasNoNullFieldsOrProperties();
                    assertThat(ObjectId.isValid(response.id())).isTrue();
                    assertThat(response.name()).isEqualTo(request.name());
                    assertThat(response.email()).isEqualTo(request.email());
                });
    }

    private static Stream<Arguments> checkConstraintsErrorsTest(){
        return Stream.of(
                Arguments.of(UserRequestFactoryBot.builder().withBlankName().build(), "name"),
                Arguments.of(UserRequestFactoryBot.builder().withLargeName().build(), "name"),
                Arguments.of(UserRequestFactoryBot.builder().withBlankEmail().build(), "email"),
                Arguments.of(UserRequestFactoryBot.builder().withLargeEmail().build(), "email"),
                Arguments.of(UserRequestFactoryBot.builder().withInvalidEmail().build(), "email")
        );
    }

    @ParameterizedTest
    @MethodSource
    void checkConstraintsErrorsTest(final UserRequest request, final String fieldError){
        requestBuilderError.withUri(UriBuilder::build)
                .withBody(request)
                .generateRequestWithSimpleBody()
                .doPost()
                .isHttpStatusIsBadRequest()
                .assertBody(response ->{
                    assertThat(response).isNotNull();
                    assertThat(response.getFields()).isNotNull();
                    assertThat(response.getFields().stream().map(ErrorFieldResponse::getName).collect(Collectors.toList())).contains(fieldError);
                });
    }

}
