package br.com.study.reactivecalendar.api.controller.response;

import br.com.study.reactivecalendar.domain.document.GuestType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record GuestFindResponse(
    @JsonProperty("userId")
    @Schema(description = "identificador do convidado (referencia para coleção de usuários)", example = "62329d79fce8ce1ca26e16c2")
    String userId,
    @JsonProperty("type")
    @Schema(description = "tipo do convidado, indica se ele pode realizar alterações no evento (admin) ou só pode responder (standard)",
            enumAsRef = true, defaultValue = "ADMIN")
    GuestType type){

    @Builder
    public GuestFindResponse {}

}
