package br.com.study.reactivecalendar.domain.mapper;

import br.com.study.reactivecalendar.domain.document.AppointmentDocument;
import br.com.study.reactivecalendar.domain.dto.AppointmentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentDocument toDocument(final AppointmentDTO dto);
}
