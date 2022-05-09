package br.com.study.reactivecalendar.api.controller.response;

import br.com.study.reactivecalendar.domain.document.GuestType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record GuestSingleResponse(
    @JsonProperty("email")
    @Schema(description = "e-mail do convidado", example = "teste@teste.com")
    String email,
    @JsonProperty("type")
    @Schema(description = "tipo do convidado, indica se ele pode realizar alterações no evento (admin) ou só pode responder (standard)",
            enumAsRef = true, defaultValue = "ADMIN")
    GuestType type){

    @Builder
    public GuestSingleResponse{}

}
