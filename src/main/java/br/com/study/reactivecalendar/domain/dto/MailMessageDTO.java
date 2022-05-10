package br.com.study.reactivecalendar.domain.dto;

import br.com.study.reactivecalendar.domain.dto.mailbuilder.CancelAppointmentBuilder;
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

    public static CancelAppointmentBuilder buildCancelAppointment(){
        return new CancelAppointmentBuilder();
    }

    public CancelAppointmentBuilder toCancelAppointmentBuilder() {
        return new CancelAppointmentBuilder(destinations, subject, variables, template);
    }

}
