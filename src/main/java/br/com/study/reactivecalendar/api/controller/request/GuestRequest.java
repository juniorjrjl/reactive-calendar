package br.com.study.reactivecalendar.api.controller.request;

import br.com.study.reactivecalendar.domain.document.GuestType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class GuestRequest {

    @Schema(description = "e-mail do convidado", example = "teste@teste.com")
    private String email;
    @Schema(description = "tipo do convidado, indica se ele pode realizar alterações no evento (admin) ou só pode responder (standard)",
            enumAsRef = true, defaultValue = "ADMIN")
    private GuestType type;

    public GuestRequest(@JsonProperty("email") final String email,
                        @JsonProperty("type") final GuestType type) {
        this.email = email;
        this.type = type;
    }
}
