package br.com.study.reactivecalendar.api.controller.request;

import br.com.study.reactivecalendar.core.validation.Period;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Period
public record AppointmentUpdateRequest(
        @Schema(description = "Título do da reunião/compromisso", example = "Daily")
        @NotBlank
        @Size(min = 1, max = 255)
        String title,
        @Schema(description = "descrição do assunta a ser tratado no momento", example = "relatorio de atividades")
        @NotBlank
        @Size(min = 1, max = 255)
        String details,
        @Schema(description = "quando a reunião irá iniciar", type = "string", format = "date-time", example = "2022-04-01T10:00:00Z")
        @Period.StartDate
        OffsetDateTime startIn,
        @Schema(description = "quando a reunião irá terminar", type = "string", format = "date-time", example = "2022-04-10T10:00:00Z")
        @Period.EndDate
        OffsetDateTime endIn,
        @Schema(description = "coleção para colocar novos convidados no compromisso")
        @Validated
        Set<GuestRequest> newGuests,
        @Schema(description = "coleção para colocar e-mail dos convidados que serão removidos", example = "mario@mario.com.br")
        @Validated
        Set<@Email String> guestsToRemove
) {

    @Builder(toBuilder = true)
    public AppointmentUpdateRequest{
        newGuests = ObjectUtils.defaultIfNull(newGuests, new HashSet<>());
        guestsToRemove = ObjectUtils.defaultIfNull(guestsToRemove, new HashSet<>());
    }

}
