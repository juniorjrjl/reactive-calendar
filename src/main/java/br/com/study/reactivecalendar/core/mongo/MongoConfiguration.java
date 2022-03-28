package br.com.study.reactivecalendar.core.mongo;

import br.com.study.reactivecalendar.core.mongo.converter.DateToOffsetDateTimeConverter;
import br.com.study.reactivecalendar.core.mongo.converter.OffsetDateTimeToDateConverter;
import br.com.study.reactivecalendar.core.mongo.provider.OffsetDateTimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableMongoAuditing(dateTimeProviderRef = "dateTimeProvider")
public class MongoConfiguration {

    @Bean
    MongoCustomConversions customConversions(){
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