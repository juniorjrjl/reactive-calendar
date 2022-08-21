package br.com.study.reactivecalendar.api.controller.router.documentation;

import br.com.study.reactivecalendar.api.controller.handler.UserHandler;
import br.com.study.reactivecalendar.api.controller.request.UserRequest;
import br.com.study.reactivecalendar.api.controller.response.ProblemResponse;
import br.com.study.reactivecalendar.api.controller.response.UserSingleResponse;
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

public interface IUserRouterDoc {
    @RouterOperations({
            @RouterOperation(operation = @Operation(operationId = "findById", summary = "busca um usuário pelo seu identificador", tags = {"Users"},
                    parameters = @Parameter(in = PATH, name = "id", description = "identificador do usuário", required = true,
                            schema = @Schema(type = "string", example = "628c5ee5d87dae26a91cafa2")),
                    responses = {@ApiResponse(responseCode = "200", description = "Usuário encontrado", content = @Content(schema = @Schema(implementation = UserSingleResponse.class), mediaType = "application/json")),
                            @ApiResponse(responseCode = "400", description = "Problema nas informações enviadas na requisição", content = @Content(schema = @Schema(implementation = ProblemResponse.class), mediaType = "application/json")),
                            @ApiResponse(responseCode = "404", description = "recurso não encontrado", content = @Content(schema = @Schema(implementation = ProblemResponse.class), mediaType = "application/json"))}),
                    path = "/users/{id}", method = GET),

            @RouterOperation(operation = @Operation(operationId = "update", summary = "atualiza um usuário", tags = {"Users"},
                    parameters = @Parameter(in = PATH, name = "id", description = "identificador do usuário", required = true,
                            schema = @Schema(type = "string", example = "628c5ee5d87dae26a91cafa2")),
                    requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = UserRequest.class), mediaType = "application/json")),
                    responses = {@ApiResponse(responseCode = "200", description = "Usuário criado", content = @Content(schema = @Schema(implementation = UserSingleResponse.class), mediaType = "application/json")),
                            @ApiResponse(responseCode = "400", description = "Problema nas informações enviadas na requisição", content = @Content(schema = @Schema(implementation = ProblemResponse.class), mediaType = "application/json")),
                            @ApiResponse(responseCode = "404", description = "recurso não encontrado", content = @Content(schema = @Schema(implementation = ProblemResponse.class), mediaType = "application/json"))}),
                    path = "/users/{id}", method = PUT),

            @RouterOperation(operation = @Operation(operationId = "delete", summary = "exclui um usuário pelo seu identificador", tags = {"Users"},
                    parameters = @Parameter(in = PATH, name = "id", description = "identificador do usuário", required = true,
                            schema = @Schema(type = "string", example = "628c5ee5d87dae26a91cafa2")),
                    responses = {@ApiResponse(responseCode = "204", description = "Usuário excluido"),
                            @ApiResponse(responseCode = "400", description = "Problema nas informações enviadas na requisição", content = @Content(schema = @Schema(implementation = ProblemResponse.class), mediaType = "application/json")),
                            @ApiResponse(responseCode = "404", description = "recurso não encontrado", content = @Content(schema = @Schema(implementation = ProblemResponse.class), mediaType = "application/json"))}),
                    path = "/users/{id}", method = DELETE),

            @RouterOperation(operation = @Operation(operationId = "save", summary = "cria um usuário", tags = {"Users"},
                    parameters = @Parameter(in = PATH, name = "id", description = "identificador do usuário", required = true,
                            schema = @Schema(type = "string", example = "628c5ee5d87dae26a91cafa2")),
                    requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = UserRequest.class), mediaType = "application/json")),
                    responses = {@ApiResponse(responseCode = "201", description = "Usuário criado", content = @Content(schema = @Schema(implementation = UserSingleResponse.class), mediaType = "application/json")),
                            @ApiResponse(responseCode = "400", description = "Problema nas informações enviadas na requisição", content = @Content(schema = @Schema(implementation = ProblemResponse.class), mediaType = "application/json")),
                            @ApiResponse(responseCode = "404", description = "recurso não encontrado", content = @Content(schema = @Schema(implementation = ProblemResponse.class), mediaType = "application/json"))}),
                    path = "/users/", method = POST)
    })
    @Bean
    RouterFunction<ServerResponse> userRoute(UserHandler handler);
}
