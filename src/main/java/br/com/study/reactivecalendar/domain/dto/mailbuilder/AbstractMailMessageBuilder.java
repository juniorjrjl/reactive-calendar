package br.com.study.reactivecalendar.domain.dto.mailbuilder;

import br.com.study.reactivecalendar.domain.dto.MailMessageDTO;
import br.com.study.reactivecalendar.domain.exception.AbstractMailBuilderException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.com.study.reactivecalendar.domain.exception.MailMessageBuilderErrorMessage.ABSTRACT_MAIL_WITHOUT_REQUIRED_PROPERTY;

@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractMailMessageBuilder {

    protected static final String titleVariable = "title";
    protected static final String detailsVariable = "details";
    protected static final String startInVariable = "startIn";
    protected static final String endInVariable = "endIn";
    protected static final String ownerVariable = "owner";

    protected List<String> destinations = new ArrayList<>();
    protected String subject;
    protected Map<String, Object> variables = new HashMap<>();
    protected String template;

    abstract AbstractMailMessageBuilder destinations(final String... destinations);

    abstract AbstractMailMessageBuilder subject(final String subject);

    public MailMessageDTO build(){
        if (StringUtils.isBlank(template)){
            throw new AbstractMailBuilderException(ABSTRACT_MAIL_WITHOUT_REQUIRED_PROPERTY.template().getMessage());
        }
        if (StringUtils.isBlank(subject)){
            throw new AbstractMailBuilderException(ABSTRACT_MAIL_WITHOUT_REQUIRED_PROPERTY.subject().getMessage());
        }
        if (CollectionUtils.isEmpty(destinations)){
            throw new AbstractMailBuilderException(ABSTRACT_MAIL_WITHOUT_REQUIRED_PROPERTY.destinations().getMessage());
        }
        return new MailMessageDTO(destinations, subject, variables, template);
    }

}
