package br.com.study.reactivecalendar.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor
public class JacksonConfigStub {

    public static ObjectMapper objectMapper(){
        return new JacksonConfig().objectMapper();
    }

}
