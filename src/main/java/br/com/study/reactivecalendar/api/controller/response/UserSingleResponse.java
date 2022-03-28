package br.com.study.reactivecalendar.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UserSingleResponse{

    @Schema(description = "identificador do usuário", format = "uuid", example = "62329d79fce8ce1ca26e16c2")
    private String id;
    @Schema(description = "nome do usuário", example = "João")
    private String name;
    @Schema(description = "email do usuário", example = "joao@joao.com.br")
    private String email;

    public UserSingleResponse(@JsonProperty("id") final String id,
                              @JsonProperty("name") final String name,
                              @JsonProperty("email") final String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
