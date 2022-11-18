package br.com.study.reactivecalendar.domain.mapper;

import br.com.study.reactivecalendar.domain.dto.AppointmentDTO;
import br.com.study.reactivecalendar.domain.dto.GuestDTO;
import br.com.study.reactivecalendar.domain.dto.MailMessageDTO;
import br.com.study.reactivecalendar.domain.dto.mailbuilder.MailMessageDTOBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import java.util.Set;

import static br.com.study.reactivecalendar.domain.document.GuestType.ADMIN;
import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Mapper(componentModel = "spring", injectionStrategy = CONSTRUCTOR)
@DecoratedWith(MailMapperDecorator.class)
public interface MailMapper {

    default MailMessageDTO toMailMessageDTO(final MailMessageDTOBuilder builder, final AppointmentDTO dto,
                                            final String ...destinations){
        return buildMailMessageDTO(builder, dto, destinations).build();
    }

    @Mapping(target = "owner", expression = "java(getAdminEmail(dto.guests()))")
    MailMessageDTOBuilder buildMailMessageDTO(@MappingTarget final MailMessageDTOBuilder builder, final AppointmentDTO dto,
                                              final String ...destinations);

    default String getAdminEmail(final Set<GuestDTO> guests){
        return guests.stream().filter(guest -> guest.type().equals(ADMIN))
                .map(GuestDTO::email)
                .findFirst()
                .orElseThrow();
    }

    default String[] getGuestsEmails(final Set<GuestDTO> guests){
        return CollectionUtils.isEmpty(guests) ?
                new String[]{} :
                guests.stream().map(GuestDTO::email).toArray(String[]::new);
    }

    @Mapping(expression = "java(dto.destinations().toArray(String[]::new))", target = "to")
    @Mapping(source = "sender", target = "from")
    @Mapping(source = "dto.subject", target = "subject")
    @Mapping(ignore = true, target = "fileTypeMap")
    @Mapping(ignore = true, target = "encodeFilenames")
    @Mapping(ignore = true, target = "validateAddresses")
    @Mapping(ignore = true, target = "replyTo")
    @Mapping(ignore = true, target = "cc")
    @Mapping(ignore = true, target = "bcc")
    @Mapping(ignore = true, target = "priority")
    @Mapping(ignore = true, target = "sentDate")
    @Mapping(ignore = true, target = "text")
    MimeMessageHelper toMimeMessageHelper(@MappingTarget final MimeMessageHelper helper, final MailMessageDTO dto,
                                          final String sender, final String body) throws MessagingException;

}
