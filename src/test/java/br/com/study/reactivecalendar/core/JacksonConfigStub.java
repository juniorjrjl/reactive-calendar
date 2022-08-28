package br.com.study.reactivecalendar.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@NoArgsConstructor
public class JacksonConfigStub {

    public static ObjectMapper objectMapper(){
        return new JacksonConfig().objectMapper();
    }

    @Bean
    public ObjectMapper objectMapperStub(){
        return objectMapper();
    }

}
