package br.com.study.reactivecalendar.api.controller.request;

import br.com.study.reactivecalendar.core.validation.AppointmentWithAdmin;
import br.com.study.reactivecalendar.core.validation.Period;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.Set;

@Period
public record AppointmentRequest (
        @JsonProperty("title")
        @Schema(description = "Título do da reunião/compromisso", example = "Daily")
        @NotBlank
        @Size(min = 1, max = 255)
        String title,

        @JsonProperty("details")
        @Schema(description = "descrição do assunta a ser tratado no momento", example = "relatorio de atividades")
        @NotBlank
        @Size(min = 1, max = 255)
        String details,

        @JsonProperty("startIn")
        @Schema(description = "quando a reunião irá iniciar", type = "string", format = "date-time", example = "2022-04-01T10:00:00Z")
        @Period.StartDate
        OffsetDateTime startIn,

        @JsonProperty("endIn")
        @Schema(description = "quando a reunião irá terminar", type = "string", format = "date-time", example = "2022-04-10T10:00:00Z")
        @Period.EndDate
        OffsetDateTime endIn,

        @JsonProperty("guests")
        @Validated
        @NotEmpty
        @AppointmentWithAdmin
        Set<GuestRequest> guests){

        @Builder
        public AppointmentRequest {}

}
