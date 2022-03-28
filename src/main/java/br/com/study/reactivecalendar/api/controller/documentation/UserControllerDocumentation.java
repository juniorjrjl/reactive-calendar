package br.com.study.reactivecalendar.api.controller.documentation;

import br.com.study.reactivecalendar.api.controller.request.UserRequest;
import br.com.study.reactivecalendar.api.controller.response.UserSingleResponse;
import br.com.study.reactivecalendar.api.exceptionhandler.ProblemResponse;
import br.com.study.reactivecalendar.core.validation.MongoId;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.PutMapping;
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
public interface UserControllerDocumentation {

    @Operation(summary = "Endpoint para criar um usuário")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "o usuário foi criado")})
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    Mono<UserSingleResponse> save(@RequestBody @Valid UserRequest request);

    @Operation(summary = "Endpoint para atualizar um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "o usuário foi atualizado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponse.class))})
    })
    @PutMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    Mono<UserSingleResponse> update(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id,
                                     @RequestBody @Valid final UserRequest request);

    @Operation(summary = "Endpoint para excluir um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "o usuário foi excluido"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponse.class))})
    })
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    Mono<Void> delete(@PathVariable @Valid @MongoId(message = "{userController.id}") String id);

    @Operation(summary = "Endpoint para buscar um usuário pelo seu identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "o usuário foi encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponse.class))})
    })
    @GetMapping(value = "/{id}")
    @ResponseStatus(OK)
    Mono<UserSingleResponse> findById(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id);

}
