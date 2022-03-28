package br.com.study.reactivecalendar.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorFieldResponse {

    @Schema(description = "nome do campo que está com erro(s)", example = "nome")
    private final String name;
    @Schema(description = "descrição do erro encontrado no campo", example = "o campo nome deve ser informado")
    private final String userMessage;
    public ErrorFieldResponse(@JsonProperty("name") final String name,
                              @JsonProperty("userMessage") final String userMessage) {
        this.name = name;
        this.userMessage = userMessage;
    }

}
