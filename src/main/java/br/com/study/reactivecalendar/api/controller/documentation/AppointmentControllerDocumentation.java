package br.com.study.reactivecalendar.api.controller.documentation;

import br.com.study.reactivecalendar.api.controller.request.AppointmentRequest;
import br.com.study.reactivecalendar.api.controller.response.AppointmentSingleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@Tag(name = "Users", description = "endpoints de gerenciamento de usuário")
public interface AppointmentControllerDocumentation {

    @Operation(summary = "Endpoint para criar um evento e notificar os convidados")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "o evento foi criado e o email de convite enviado")})
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    Mono<AppointmentSingleResponse> save(@RequestBody @Valid AppointmentRequest request);

}
