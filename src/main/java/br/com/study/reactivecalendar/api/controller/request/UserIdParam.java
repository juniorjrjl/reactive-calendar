package br.com.study.reactivecalendar.api.controller.request;

import br.com.study.reactivecalendar.core.validation.MongoId;

public record UserIdParam (
        @MongoId
        String id
){

}