package br.com.study.reactivecalendar.domain.dto;

import br.com.study.reactivecalendar.domain.dto.mailbuilder.MailMessageDTOBuilder;

import java.util.List;
import java.util.Map;

public record MailMessageDTO(List<String> destinations, String subject, Map<String, Object> variables,
                             String template) {

    public static MailMessageDTOBuilder buildNewAppointment() {
        return new MailMessageDTOBuilder("Você recebeu um convite para um evento", "mail/newInvite");
    }

    public static MailMessageDTOBuilder buildEditAppointment(){
        return new MailMessageDTOBuilder("Um evento que você foi convidado foi alterado", "mail/editInvite");
    }

    public static MailMessageDTOBuilder buildCancelAppointment(){
        return new MailMessageDTOBuilder("Um evento foi cancelado", "mail/cancelInvite");
    }

    public static MailMessageDTOBuilder buildRemoveAppointment(){
        return new MailMessageDTOBuilder("Você foi removido de um enveto", "mail/removeInvite");
    }

}
