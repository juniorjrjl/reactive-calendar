package br.com.study.reactivecalendar.api.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record ProblemResponse (@Schema(description = "http status retornado no cabeçalho do response", format = "number", example = "200")
                              @JsonProperty("status")
                              Integer status,
                              @Schema(description = "data e hora do response", format = "date-time", example = "2022-01-18T23:50.000Z")
                              @JsonProperty("timestamp")
                              OffsetDateTime timestamp,
                              @Schema(description = "descrição do erro", example = "a requisição contém erros")
                              @JsonProperty("errorDescription")
                              String errorDescription,
                              @Schema(description = "caso tenha erros no corpo da requisição eles serão apresentados aqui")
                              @JsonProperty("fields")
                              List<ErrorFieldResponse> fields){

    @Builder(toBuilder = true)
    public ProblemResponse { }

}
