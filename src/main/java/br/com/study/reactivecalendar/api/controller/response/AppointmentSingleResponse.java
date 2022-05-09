package br.com.study.reactivecalendar.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Set;

public record AppointmentSingleResponse(
    @JsonProperty("id")
    @Schema(description = "identificador do compromisso", format = "uuid", example = "62329d79fce8ce1ca26e16c2")
    String id,
    @JsonProperty("title")
    @Schema(description = "Título do da reunião/compromisso", example = "Daily")
    String title,
    @JsonProperty("details")
    @Schema(description = "descrição do assunta a ser tratado no momento", example = "relatorio de atividades")
    String details,
    @JsonProperty("startIn")
    @Schema(description = "quando a reunião irá iniciar", type = "string", format = "date-time", example = "2022-04-01T10:00:00Z")
    OffsetDateTime startIn,
    @JsonProperty("endInd")
    @Schema(description = "quando a reunião irá terminar", type = "string", format = "date-time", example = "2022-04-10T10:00:00Z")
    OffsetDateTime endIn,
    @JsonProperty("guests")
    Set<GuestSingleResponse> guests
){
    @Builder
    public AppointmentSingleResponse {}

}
