package br.com.study.reactivecalendar.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
@Builder
public class ProblemResponse {

    @Schema(description = "http status retornado no cabeçalho do response", format = "number", example = "200")
    private final Integer status;
    @Schema(description = "data e hora do response", format = "date-time", example = "2022-01-18T23:50.000Z")
    private final OffsetDateTime timestamp;
    @Schema(description = "descrição do erro", example = "a requisição contém erros")
    private final String errorDescription;
    @Schema(description = "caso tenha erros no corpo da requisição eles serão apresentados aqui")
    private final List<ErrorFieldResponse> fields;

    public ProblemResponse(@JsonProperty("status") final Integer status,
                           @JsonProperty("timestamp") final OffsetDateTime timestamp,
                           @JsonProperty("errorDescription") final String errorDescription,
                           @JsonProperty("fields") final List<ErrorFieldResponse> fields) {
        this.status = status;
        this.timestamp = timestamp;
        this.errorDescription = errorDescription;
        this.fields = fields;
    }
}
