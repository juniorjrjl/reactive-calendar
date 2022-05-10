package br.com.study.reactivecalendar.domain.dto.mailbuilder;

import br.com.study.reactivecalendar.domain.dto.MailMessageDTO;
import br.com.study.reactivecalendar.domain.exception.CancelAppointmentMailBuilderException;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static br.com.study.reactivecalendar.domain.exception.MailMessageBuilderErrorMessage.CANCEL_INVITE_WITHOUT_REQUIRED_PROPERTY;

public class CancelAppointmentBuilder extends AbstractMailMessageBuilder{

    public CancelAppointmentBuilder(final List<String> destinations, final String subject, final Map<String, Object> variables, final String template) {
        super(destinations, subject, variables, template);
    }

    public CancelAppointmentBuilder() {
        this.template = "mail/cancelInvite";
    }

    public CancelAppointmentBuilder destinations(final String... destinations){
        this.destinations.addAll(Arrays.stream(destinations).toList());
        return this;
    }

    public CancelAppointmentBuilder subject(final String subject){
        this.subject = subject;
        return this;
    }

    private CancelAppointmentBuilder variable(final String key, final Object value){
        variables.put(key, value);
        return this;
    }

    public CancelAppointmentBuilder title(final String title){
        return variable(titleVariable, title);
    }

    public CancelAppointmentBuilder details(final String details){
        return variable(detailsVariable, details);
    }

    public CancelAppointmentBuilder startIn(final OffsetDateTime startIn){
        return variable(startInVariable, startIn);
    }

    public CancelAppointmentBuilder endIn(final OffsetDateTime endIn){
        return variable(endInVariable, endIn);
    }

    public CancelAppointmentBuilder owner(final String owner){
        return variable(ownerVariable, owner);
    }

    @Override
    public MailMessageDTO build() {
        if (!variables.containsKey(titleVariable)){
            throw new CancelAppointmentMailBuilderException(CANCEL_INVITE_WITHOUT_REQUIRED_PROPERTY.title().getMessage());
        }
        if (!variables.containsKey(detailsVariable)){
            throw new CancelAppointmentMailBuilderException(CANCEL_INVITE_WITHOUT_REQUIRED_PROPERTY.details().getMessage());
        }
        if (!variables.containsKey(startInVariable)){
            throw new CancelAppointmentMailBuilderException(CANCEL_INVITE_WITHOUT_REQUIRED_PROPERTY.startIn().getMessage());
        }
        if (!variables.containsKey(endInVariable)){
            throw new CancelAppointmentMailBuilderException(CANCEL_INVITE_WITHOUT_REQUIRED_PROPERTY.endIn().getMessage());
        }
        if (!variables.containsKey(ownerVariable)){
            throw new CancelAppointmentMailBuilderException(CANCEL_INVITE_WITHOUT_REQUIRED_PROPERTY.owner().getMessage());
        }
        return super.build();
    }

}
