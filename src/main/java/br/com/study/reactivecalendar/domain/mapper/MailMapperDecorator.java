package br.com.study.reactivecalendar.domain.mapper;

import br.com.study.reactivecalendar.domain.dto.MailMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;

public abstract class MailMapperDecorator implements MailMapper {

    @Autowired
    @Qualifier("delegate")
    private MailMapper mailMapper;

    @Override
    public MimeMessageHelper toMimeMessageHelper(final MimeMessageHelper helper, final MailMessageDTO dto,
                                                 final String sender, final String body) throws MessagingException {
        mailMapper.toMimeMessageHelper(helper, dto, sender, body);
        helper.setText(body, true);
        return helper;
    }
}