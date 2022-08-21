package br.com.study.reactivecalendar.api.controller.router.documentation;

import br.com.study.reactivecalendar.api.controller.handler.AppointmentHandler;
import br.com.study.reactivecalendar.api.controller.request.AppointmentRequest;
import br.com.study.reactivecalendar.api.controller.request.AppointmentUpdateRequest;
import br.com.study.reactivecalendar.api.controller.response.AppointmentSingleResponse;
import br.com.study.reactivecalendar.api.controller.response.ProblemResponse;
import br.com.study.reactivecalendar.domain.document.AppointmentDocument;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

public interface IAppointmentRouterDoc {

    @RouterOperations({
            @RouterOperation(operation = @Operation(operationId = "findById", summary = "busca um usuário pelo seu identificador", tags = {"Appointments"},
                    parameters = @Parameter(in = PATH, name = "id", description = "identificador do compromisso", required = true,
                            schema = @Schema(type = "string", example = "628c5ee5d87dae26a91cafa2")),
                    responses = {@ApiResponse(responseCode = "200", description = "Compromisso encontrado", content = @Content(schema = @Schema(implementation = AppointmentDocument.class), mediaType = "application/json")),
                            @ApiResponse(responseCode = "400", description = "Problema nas informações enviadas na requisição", content = @Content(schema = @Schema(implementation = ProblemResponse.class), mediaType = "application/json")),
                            @ApiResponse(responseCode = "404", description = "recurso não encontrado", content = @Content(schema = @Schema(implementation = ProblemResponse.class), mediaType = "application/json"))}),
                    path = "/appointments/{id}", method = GET),

            @RouterOperation(operation = @Operation(operationId = "update", summary = "atualiza um compromisso e notifica os convidados", tags = {"Appointments"},
                    parameters = @Parameter(in = PATH, name = "id", description = "identificador do usuário", required = true,
                            schema = @Schema(type = "string", example = "628c5ee5d87dae26a91cafa2")),
                    requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = AppointmentUpdateRequest.class), mediaType = "application/json")),
                    responses = {@ApiResponse(responseCode = "200", description = "Compromisso atualizado", content = @Content(schema = @Schema(implementation = AppointmentSingleResponse.class), mediaType = "application/json")),
                            @ApiResponse(responseCode = "400", description = "Problema nas informações enviadas na requisição", content = @Content(schema = @Schema(implementation = ProblemResponse.class), mediaType = "application/json")),
                            @ApiResponse(responseCode = "404", description = "recurso não encontrado", content = @Content(schema = @Schema(implementation = ProblemResponse.class), mediaType = "application/json"))}),
                    path = "/appointments/{id}", method = PUT),

            @RouterOperation(operation = @Operation(operationId = "delete", summary = "exclui um compromisso pelo seu identificador e notifica os convidados", tags = {"Appointments"},
                    parameters = @Parameter(in = PATH, name = "id", description = "identificador do compromisso", required = true,
                            schema = @Schema(type = "string", example = "628c5ee5d87dae26a91cafa2")),
                    responses = {@ApiResponse(responseCode = "204", description = "Compromisso excluido"),
                            @ApiResponse(responseCode = "400", description = "Problema nas informações enviadas na requisição", content = @Content(schema = @Schema(implementation = ProblemResponse.class), mediaType = "application/json")),
                            @ApiResponse(responseCode = "404", description = "recurso não encontrado", content = @Content(schema = @Schema(implementation = ProblemResponse.class), mediaType = "application/json"))}),
                    path = "/appointments/{id}", method = DELETE),

            @RouterOperation(operation = @Operation(operationId = "save", summary = "cria um novo compromisso e envia e-mail para os usuários comvidados", tags = {"Appointments"},
                    requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = AppointmentRequest.class), mediaType = "application/json")),
                    responses = {@ApiResponse(responseCode = "201", description = "Compromisso criado", content = @Content(schema = @Schema(implementation = AppointmentSingleResponse.class), mediaType = "application/json")),
                            @ApiResponse(responseCode = "400", description = "Problema nas informações enviadas na requisição", content = @Content(schema = @Schema(implementation = ProblemResponse.class), mediaType = "application/json")),
                            @ApiResponse(responseCode = "404", description = "recurso não encontrado", content = @Content(schema = @Schema(implementation = ProblemResponse.class), mediaType = "application/json"))}),
                    path = "/appointments/", method = POST)
    })
    @Bean
    RouterFunction<ServerResponse> appointmentRoute(AppointmentHandler handler);
}
