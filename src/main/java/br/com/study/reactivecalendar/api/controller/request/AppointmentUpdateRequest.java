package br.com.study.reactivecalendar.api.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.ObjectUtils;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

public record AppointmentUpdateRequest(
        @Schema(description = "Título do da reunião/compromisso", example = "Daily")
        String title,
        @Schema(description = "descrição do assunta a ser tratado no momento", example = "relatorio de atividades")
        String details,
        @Schema(description = "quando a reunião irá iniciar", type = "string", format = "date-time", example = "2022-04-01T10:00:00Z")
        OffsetDateTime startIn,
        @Schema(description = "quando a reunião irá terminar", type = "string", format = "date-time", example = "2022-04-10T10:00:00Z")
        OffsetDateTime endIn,
        @Schema(description = "coleção para colocar novos convidados no compromisso")
        Set<GuestRequest> newGuests,
        @Schema(description = "coleção para colocar e-mail dos convidados que serão removidos", example = "mario@mario.com.br")
        Set<String> guestsToRemove
) {

    public AppointmentUpdateRequest{
        newGuests = ObjectUtils.defaultIfNull(newGuests, new HashSet<>());
        guestsToRemove = ObjectUtils.defaultIfNull(guestsToRemove, new HashSet<>());
    }

}
