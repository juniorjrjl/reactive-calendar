package br.com.study.reactivecalendar.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record UserRequest(

        @JsonProperty("name")
        @Schema(description = "nome do usuário", example = "João") @NotBlank @Size(min = 1, max = 255) String name,
        @JsonProperty("email")
        @Schema(description = "email do usuário", example = "joao@joao.com.br") @NotBlank @Email @Size(min = 1, max = 255) String email) {

    @Builder
    public UserRequest {}

}
