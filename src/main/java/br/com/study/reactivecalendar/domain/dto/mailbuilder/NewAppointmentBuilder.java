package br.com.study.reactivecalendar.domain.dto.mailbuilder;

import br.com.study.reactivecalendar.domain.dto.MailMessageDTO;
import br.com.study.reactivecalendar.domain.exception.NewAppointmentMailBuilderException;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static br.com.study.reactivecalendar.domain.exception.MailMessageBuilderErrorMessage.NEW_INVITE_WITHOUT_REQUIRED_PROPERTY;

public class NewAppointmentBuilder extends AbstractMailMessageBuilder{

    public NewAppointmentBuilder(final List<String> destinations, final String subject, final Map<String, Object> variables, final String template) {
        super(destinations, subject, variables, template);
    }

    public NewAppointmentBuilder() {
        this.template = "mail/newInvite";
    }

    public NewAppointmentBuilder destinations(final String... destinations){
        this.destinations.addAll(Arrays.stream(destinations).toList());
        return this;
    }

    public NewAppointmentBuilder subject(final String subject){
        this.subject = subject;
        return this;
    }

    private NewAppointmentBuilder variable(final String key, final Object value){
        variables.put(key, value);
        return this;
    }

    public NewAppointmentBuilder title(final String title){
        return variable(titleVariable, title);
    }

    public NewAppointmentBuilder details(final String details){
        return variable(detailsVariable, details);
    }

    public NewAppointmentBuilder startIn(final OffsetDateTime startIn){
        return variable(startInVariable, startIn);
    }

    public NewAppointmentBuilder endIn(final OffsetDateTime endIn){
        return variable(endInVariable, endIn);
    }

    public NewAppointmentBuilder owner(final String owner){
        return variable(ownerVariable, owner);
    }

    @Override
    public MailMessageDTO build() {
        if (!variables.containsKey(titleVariable)){
            throw new NewAppointmentMailBuilderException(NEW_INVITE_WITHOUT_REQUIRED_PROPERTY.title().getMessage());
        }
        if (!variables.containsKey(detailsVariable)){
            throw new NewAppointmentMailBuilderException(NEW_INVITE_WITHOUT_REQUIRED_PROPERTY.details().getMessage());
        }
        if (!variables.containsKey(startInVariable)){
            throw new NewAppointmentMailBuilderException(NEW_INVITE_WITHOUT_REQUIRED_PROPERTY.startIn().getMessage());
        }
        if (!variables.containsKey(endInVariable)){
            throw new NewAppointmentMailBuilderException(NEW_INVITE_WITHOUT_REQUIRED_PROPERTY.endIn().getMessage());
        }
        if (!variables.containsKey(ownerVariable)){
            throw new NewAppointmentMailBuilderException(NEW_INVITE_WITHOUT_REQUIRED_PROPERTY.owner().getMessage());
        }
        return super.build();
    }

}
