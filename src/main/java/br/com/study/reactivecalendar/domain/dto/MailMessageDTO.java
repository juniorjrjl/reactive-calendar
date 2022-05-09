package br.com.study.reactivecalendar.domain.dto;

import br.com.study.reactivecalendar.domain.dto.mailbuilder.NewAppointmentBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class MailMessageDTO {

    private final List<String> destinations;
    private final String subject;
    private final Map<String, Object> variables;
    private final String template;

    public static NewAppointmentBuilder buildNewAppointment(){
        return new NewAppointmentBuilder();
    }

    public NewAppointmentBuilder toNewAppointmentBuilder(){
        return new NewAppointmentBuilder(destinations, subject, variables, template);
    }

}
