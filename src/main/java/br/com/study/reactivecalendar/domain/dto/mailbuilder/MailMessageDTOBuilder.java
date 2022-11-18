package br.com.study.reactivecalendar.domain.dto.mailbuilder;

import br.com.study.reactivecalendar.domain.dto.MailMessageDTO;
import br.com.study.reactivecalendar.domain.exception.MailBuilderException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.com.study.reactivecalendar.domain.exception.MailMessageBuilderErrorMessage.MAIL_WITHOUT_REQUIRED_PROPERTY;

@RequiredArgsConstructor
public class MailMessageDTOBuilder {

    private static final String titleVariable = "title";
    private static final String detailsVariable = "details";
    private static final String startInVariable = "startIn";
    private static final String endInVariable = "endIn";
    private static final String ownerVariable = "owner";

    private final List<String> destinations = new ArrayList<>();
    private final String subject;
    private final Map<String, Object> variables = new HashMap<>();
    private final String template;

    public MailMessageDTOBuilder destinations(final String... destinations){
        this.destinations.addAll(Arrays.stream(destinations).toList());
        return this;
    }

    private MailMessageDTOBuilder variable(final String key, final Object value){
        variables.put(key, value);
        return this;
    }

    public MailMessageDTOBuilder title(final String title){
        return variable(titleVariable, title);
    }

    public MailMessageDTOBuilder details(final String details){
        return variable(detailsVariable, details);
    }

    public MailMessageDTOBuilder startIn(final OffsetDateTime startIn){
        return variable(startInVariable, startIn);
    }

    public MailMessageDTOBuilder endIn(final OffsetDateTime endIn){
        return variable(endInVariable, endIn);
    }

    public MailMessageDTOBuilder owner(final String owner){
        return variable(ownerVariable, owner);
    }

    public MailMessageDTO build(){
        if (StringUtils.isBlank(template)){
            throw new MailBuilderException(MAIL_WITHOUT_REQUIRED_PROPERTY.template().getMessage());
        }
        if (StringUtils.isBlank(subject)){
            throw new MailBuilderException(MAIL_WITHOUT_REQUIRED_PROPERTY.subject().getMessage());
        }
        if (CollectionUtils.isEmpty(destinations)){
            throw new MailBuilderException(MAIL_WITHOUT_REQUIRED_PROPERTY.destinations().getMessage());
        }
        if (!variables.containsKey(titleVariable)){
            throw new MailBuilderException(MAIL_WITHOUT_REQUIRED_PROPERTY.title().getMessage());
        }
        if (!variables.containsKey(detailsVariable)){
            throw new MailBuilderException(MAIL_WITHOUT_REQUIRED_PROPERTY.details().getMessage());
        }
        if (!variables.containsKey(startInVariable)){
            throw new MailBuilderException(MAIL_WITHOUT_REQUIRED_PROPERTY.startIn().getMessage());
        }
        if (!variables.containsKey(endInVariable)){
            throw new MailBuilderException(MAIL_WITHOUT_REQUIRED_PROPERTY.endIn().getMessage());
        }
        if (!variables.containsKey(ownerVariable)){
            throw new MailBuilderException(MAIL_WITHOUT_REQUIRED_PROPERTY.owner().getMessage());
        }
        return new MailMessageDTO(destinations, subject, variables, template);
    }

}
