package br.com.study.reactivecalendar.domain.document;

import lombok.Builder;

public record Guest(String userId, GuestType type){

    @Builder(toBuilder = true)
    public Guest {}

}
