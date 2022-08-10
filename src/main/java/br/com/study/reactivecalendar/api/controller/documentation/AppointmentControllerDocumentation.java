package br.com.study.reactivecalendar.api.controller.documentation;

import br.com.study.reactivecalendar.api.controller.request.AppointmentRequest;
import br.com.study.reactivecalendar.api.controller.response.AppointmentFindResponse;
import br.com.study.reactivecalendar.api.controller.response.AppointmentSingleResponse;
import br.com.study.reactivecalendar.api.controller.response.ProblemResponse;
import br.com.study.reactivecalendar.core.validation.MongoId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@Tag(name = "Users", description = "endpoints de gerenciamento de usuário")
public interface AppointmentControllerDocumentation {

    @Operation(summary = "Endpoint para criar um evento e notificar os convidados")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "o evento foi criado e o email de convite enviado")})
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    Mono<AppointmentSingleResponse> save(@RequestBody @Valid AppointmentRequest request);


    @Operation(summary = "Endpoint para buscar um evento pelo seu identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "o evento foi encontrado"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponse.class))})
    })
    @GetMapping(value = "/{id}")
    @ResponseStatus(OK)
    Mono<AppointmentFindResponse> findById(@PathVariable @Valid @Parameter(description = "Identificador do evento", example = "610999d015f9b63acc77f317")
                                           @MongoId(message = "{appointmentController.id}") final String id);

    @Operation(summary = "Endpoint para excluir um evento pelo seu identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "o evento foi excluido"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponse.class))})
    })
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    Mono<Void> delete(@PathVariable @Valid @Parameter(description = "Identificador do evento", example = "610999d015f9b63acc77f317")
                      @MongoId(message = "{appointmentController.id}") final String id);

}
