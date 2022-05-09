package br.com.study.reactivecalendar.domain.dto;

import br.com.study.reactivecalendar.domain.document.GuestType;
import lombok.Builder;

public record GuestDTO(String userId,
                       String email,
                       GuestType type) {

    @Builder
    public GuestDTO {}

}