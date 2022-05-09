package br.com.study.reactivecalendar.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.Set;

@Getter
@Builder
@ToString
public class AppointmentRequest {

    @Schema(description = "Título do da reunião/compromisso", example = "Daily")
    private String title;
    @Schema(description = "descrição do assunta a ser tratado no momento", example = "relatorio de atividades")
    private String details;
    @Schema(description = "quando a reunião irá iniciar", type = "string", format = "date-time", example = "2022-04-01T10:00:00Z")
    private OffsetDateTime startIn;
    @Schema(description = "quando a reunião irá terminar", type = "string", format = "date-time", example = "2022-04-10T10:00:00Z")
    private OffsetDateTime endIn;
    private Set<GuestRequest> guests;

    public AppointmentRequest(@JsonProperty("title") final String title,
                              @JsonProperty("details") final String details,
                              @JsonProperty("startIn") final OffsetDateTime startIn,
                              @JsonProperty("endInd") final OffsetDateTime endIn,
                              @JsonProperty("guests") final Set<GuestRequest> guests) {
        this.title = title;
        this.details = details;
        this.startIn = startIn;
        this.endIn = endIn;
        this.guests = guests;
    }
}
