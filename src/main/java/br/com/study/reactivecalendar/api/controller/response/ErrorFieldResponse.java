package br.com.study.reactivecalendar.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record ErrorFieldResponse(@Schema(description = "nome do campo que está com erro(s)", example = "nome")
                                 @JsonProperty("name")
                                 String name,
                                 @Schema(description = "descrição do erro encontrado no campo", example = "o campo nome deve ser informado")
                                 @JsonProperty("userMessage")
                                 String userMessage){

    @Builder(toBuilder = true)
    public ErrorFieldResponse { }

}
