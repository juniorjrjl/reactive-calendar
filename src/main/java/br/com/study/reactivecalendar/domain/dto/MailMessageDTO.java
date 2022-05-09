package br.com.study.reactivecalendar.domain.dto;

import br.com.study.reactivecalendar.domain.dto.mailbuilder.NewAppointmentBuilder;

import java.util.List;
import java.util.Map;

public record MailMessageDTO(List<String> destinations, String subject, Map<String, Object> variables,
                             String template) {

    public static NewAppointmentBuilder buildNewAppointment() {
        return new NewAppointmentBuilder();
    }

    public NewAppointmentBuilder toNewAppointmentBuilder() {
        return new NewAppointmentBuilder(destinations, subject, variables, template);
    }

}
