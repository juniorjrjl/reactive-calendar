package br.com.study.reactivecalendar.api.controller.response;

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
public class AppointmentSingleResponse {

    @Schema(description = "identificador do compromisso", format = "uuid", example = "62329d79fce8ce1ca26e16c2")
    private String id;
    @Schema(description = "Título do da reunião/compromisso", example = "Daily")
    private String title;
    @Schema(description = "descrição do assunta a ser tratado no momento", example = "relatorio de atividades")
    private String details;
    @Schema(description = "quando a reunião irá iniciar", type = "string", format = "date-time", example = "2022-04-01T10:00:00Z")
    private OffsetDateTime startIn;
    @Schema(description = "quando a reunião irá terminar", type = "string", format = "date-time", example = "2022-04-10T10:00:00Z")
    private OffsetDateTime endIn;
    private Set<GuestSingleResponse> guests;

    public AppointmentSingleResponse(@JsonProperty("id") final String id,
                                     @JsonProperty("title") final String title,
                                     @JsonProperty("details") final String details,
                                     @JsonProperty("startIn") final OffsetDateTime startIn,
                                     @JsonProperty("endInd") final OffsetDateTime endIn,
                                     @JsonProperty("guests") final Set<GuestSingleResponse> guests) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.startIn = startIn;
        this.endIn = endIn;
        this.guests = guests;
    }
}
