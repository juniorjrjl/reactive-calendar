package br.com.study.reactivecalendar.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record UserSingleResponse(

    @JsonProperty("id")
    @Schema(description = "identificador do usuário", format = "uuid", example = "62329d79fce8ce1ca26e16c2")
    String id,
    @JsonProperty("name")
    @Schema(description = "nome do usuário", example = "João")
    String name,
    @JsonProperty("email")
    @Schema(description = "email do usuário", example = "joao@joao.com.br")
    String email){

    @Builder
    public UserSingleResponse {}
}

