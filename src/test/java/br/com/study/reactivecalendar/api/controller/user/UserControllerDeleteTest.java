package br.com.study.reactivecalendar.api.controller.user;

import br.com.study.reactivecalendar.ReactiveCalendarApplication;
import br.com.study.reactivecalendar.api.controller.UserController;
import br.com.study.reactivecalendar.api.controller.response.ProblemResponse;
import br.com.study.reactivecalendar.api.mapper.UserMapperImpl;
import br.com.study.reactivecalendar.core.EmbeddedMongoDbConfig;
import br.com.study.reactivecalendar.core.factoryBot.document.UserDocumentFactoryBot;
import br.com.study.reactivecalendar.domain.document.UserDocument;
import br.com.study.reactivecalendar.domain.repository.UserRepository;
import br.com.study.reactivecalendar.domain.service.UserService;
import br.com.study.reactivecalendar.domain.service.query.UserQueryService;
import br.com.study.reactivecalendar.utils.request.RequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.com.study.reactivecalendar.core.BotGenerator.userDocumentBotGenerator;
import static br.com.study.reactivecalendar.core.RandomData.getFaker;
import static br.com.study.reactivecalendar.core.RandomData.randomMongoId;
import static br.com.study.reactivecalendar.utils.request.RequestBuilderInstances.noBodyResponseRequestBuilder;
import static br.com.study.reactivecalendar.utils.request.RequestBuilderInstances.problemResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ContextConfiguration(classes = {ReactiveCalendarApplication.class, UserMapperImpl.class, UserQueryService.class,
        UserService.class, UserRepository.class, EmbeddedMongoDbConfig.class})
@WebFluxTest({UserController.class, UserRepository.class})
@AutoConfigureDataMongo
@ExtendWith({SpringExtension.class})
public class UserControllerDeleteTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    private RequestBuilder<Void> requestBuilder;
    private RequestBuilder<ProblemResponse> requestBuilderError;
    private UserDocument userInserted;

    @BeforeEach
    void setup(){
        requestBuilder = noBodyResponseRequestBuilder(applicationContext, "/users");
        requestBuilderError = problemResponseRequestBuilder(applicationContext, "/users");
        userInserted = userDocumentBotGenerator.generate(() -> UserDocumentFactoryBot.builder().build(), document -> userRepository.save(document).block());
    }

    @Test
    void findByIdTest(){
        requestBuilder.withUri(uriBuilder -> uriBuilder.pathSegment("{id}").build(userInserted.id()))
                .generateRequestWithSimpleBody()
                .doDelete()
                .isHttpStatusIsNoContent();
        assertThat(userRepository.findById(userInserted.id()).blockOptional().isEmpty()).isTrue();
    }

    @Test
    void whenTryToFindByNoStoredIdThenReturnNotFound(){
        requestBuilderError.withUri(uriBuilder -> uriBuilder.pathSegment("{id}").build(randomMongoId()))
                .generateRequestWithSimpleBody()
                .doDelete()
                .isHttpStatusIsNotFound();
    }

    @Test
    void whenUseInvalidIdThenReturnBadRequest(){
        requestBuilderError.withUri(uriBuilder -> uriBuilder.pathSegment("{id}").build(getFaker().lorem().word()))
                .generateRequestWithSimpleBody()
                .doDelete()
                .isHttpStatusIsBadRequest();
    }

}
