package br.com.study.reactivecalendar.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
@ToString
public class UserRequest {

    @Schema(description = "nome do usuário", example = "João")
    @NotBlank
    @Size(min = 1, max = 255)
    private final String name;

    @Schema(description = "email do usuário", example = "joao@joao.com.br")
    @NotBlank
    @Email
    @Size(min = 1, max = 255)
    private final String email;

    public UserRequest(@JsonProperty("name") final String name,
                       @JsonProperty("email") final String email) {
        this.name = name;
        this.email = email;
    }

}
