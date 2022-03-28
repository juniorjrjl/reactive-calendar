package br.com.study.reactivecalendar.api.controller.user;

import br.com.study.reactivecalendar.ReactiveCalendarApplication;
import br.com.study.reactivecalendar.api.controller.UserController;
import br.com.study.reactivecalendar.api.controller.request.UserRequest;
import br.com.study.reactivecalendar.api.controller.response.UserSingleResponse;
import br.com.study.reactivecalendar.api.exceptionhandler.ErrorFieldResponse;
import br.com.study.reactivecalendar.api.exceptionhandler.ProblemResponse;
import br.com.study.reactivecalendar.api.mapper.UserMapperImpl;
import br.com.study.reactivecalendar.core.EmbeddedMongoDbConfig;
import br.com.study.reactivecalendar.core.factoryBot.document.UserDocumentFactoryBot;
import br.com.study.reactivecalendar.core.factoryBot.request.UserRequestFactoryBot;
import br.com.study.reactivecalendar.domain.document.UserDocument;
import br.com.study.reactivecalendar.domain.repository.UserRepository;
import br.com.study.reactivecalendar.domain.service.UserService;
import br.com.study.reactivecalendar.domain.service.query.UserQueryService;
import br.com.study.reactivecalendar.utils.request.RequestBuilder;
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

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.study.reactivecalendar.core.BotGenerator.userDocumentBotGenerator;
import static br.com.study.reactivecalendar.core.RandomData.getFaker;
import static br.com.study.reactivecalendar.core.RandomData.randomMongoId;
import static br.com.study.reactivecalendar.utils.request.RequestBuilderInstances.problemResponseRequestBuilder;
import static br.com.study.reactivecalendar.utils.request.RequestBuilderInstances.userResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ContextConfiguration(classes = {ReactiveCalendarApplication.class, UserMapperImpl.class, UserQueryService.class,
        UserService.class, UserRepository.class, EmbeddedMongoDbConfig.class})
@WebFluxTest({UserController.class, UserRepository.class})
@AutoConfigureDataMongo
@ExtendWith({SpringExtension.class})
public class UserControllerUpdateTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    private RequestBuilder<UserSingleResponse> requestBuilder;
    private RequestBuilder<ProblemResponse> requestBuilderError;
    private UserDocument userInserted;

    @BeforeEach
    void setup(){
        requestBuilder = userResponseRequestBuilder(applicationContext, "/users");
        requestBuilderError = problemResponseRequestBuilder(applicationContext, "/users");
        userInserted = userDocumentBotGenerator.generate(() -> UserDocumentFactoryBot.builder().build(), document -> userRepository.save(document).block());
    }

    @Test
    void updateTest(){
        var request = UserRequestFactoryBot.builder().build();
        requestBuilder.withUri(uriBuilder -> uriBuilder.pathSegment("{id}").build(userInserted.getId()))
                .withBody(request)
                .generateRequestWithSimpleBody()
                .doPut()
                .isHttpStatusIsOk()
                .assertBody(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response).hasNoNullFieldsOrProperties();
                    assertThat(response.getId()).isEqualTo(userInserted.getId());
                    assertThat(response.getName()).isEqualTo(request.getName());
                    assertThat(response.getEmail()).isEqualTo(request.getEmail());
                });
    }

    @Test
    void whenTryToFindByNoStoredIdThenReturnNotFound(){
        var request = UserRequestFactoryBot.builder().build();
        requestBuilderError.withUri(uriBuilder -> uriBuilder.pathSegment("{id}").build(randomMongoId()))
                .withBody(request)
                .generateRequestWithSimpleBody()
                .doPut()
                .isHttpStatusIsNotFound();
    }

    @Test
    void whenUseInvalidIdThenReturnBadRequest(){
        var request = UserRequestFactoryBot.builder().build();
        requestBuilderError.withUri(uriBuilder -> uriBuilder.pathSegment("{id}").build(getFaker().lorem().word()))
                .withBody(request)
                .generateRequestWithSimpleBody()
                .doPut()
                .isHttpStatusIsBadRequest();
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
        requestBuilderError.withUri(uriBuilder -> uriBuilder.pathSegment("{id}").build(randomMongoId()))
                .withBody(request)
                .generateRequestWithSimpleBody()
                .doPut()
                .isHttpStatusIsBadRequest()
                .assertBody(response ->{
                    assertThat(response).isNotNull();
                    assertThat(response.getFields()).isNotNull();
                    assertThat(response.getFields().stream().map(ErrorFieldResponse::getName).collect(Collectors.toList())).contains(fieldError);
                });
    }

}
