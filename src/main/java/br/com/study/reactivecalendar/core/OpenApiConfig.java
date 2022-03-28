package br.com.study.reactivecalendar.core;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Reactive Calendar", description = "API reativa baseada no calendário do google"),
        servers = {
                @Server(url = "http://localhost:8080/reactive-calendar", description = "local")
        }
)
public class OpenApiConfig {
}
