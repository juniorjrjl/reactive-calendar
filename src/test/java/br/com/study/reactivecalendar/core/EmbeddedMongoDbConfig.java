package br.com.study.reactivecalendar.core;

import br.com.study.reactivecalendar.core.mongo.converter.DateToOffsetDateTimeConverter;
import br.com.study.reactivecalendar.core.mongo.converter.OffsetDateTimeToDateConverter;
import br.com.study.reactivecalendar.core.mongo.provider.OffsetDateTimeProvider;
import br.com.study.reactivecalendar.domain.repository.UserRepository;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@TestConfiguration
@EnableReactiveMongoRepositories(basePackageClasses = UserRepository.class)
@AllArgsConstructor
public class EmbeddedMongoDbConfig extends AbstractReactiveMongoConfiguration {

    private final Environment environment;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create();
    }

    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Bean
    ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(mongoClient(), getDatabaseName());
    }

    @Override
    public MongoCustomConversions customConversions() {
        List<Converter<? ,?>> converters = new ArrayList<>();
        converters.add(new DateToOffsetDateTimeConverter());
        converters.add(new OffsetDateTimeToDateConverter());
        return new MongoCustomConversions(converters);
    }

    @Bean
    DateTimeProvider dateTimeProvider(){
        return new OffsetDateTimeProvider();
    }

}
