package br.com.study.reactivecalendar.domain.mapper;

import br.com.study.reactivecalendar.domain.document.AppointmentDocument;
import br.com.study.reactivecalendar.domain.document.UserDocument;
import br.com.study.reactivecalendar.domain.dto.AppointmentDTO;
import br.com.study.reactivecalendar.domain.dto.GuestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentDocument toDocument(final AppointmentDTO dto);

    AppointmentDTO toDTO(final AppointmentDocument document);

    default GuestDTO setEmail(final GuestDTO dto, final UserDocument document){
        return dto.toBuilder().email(document.email()).build();
    }

}
