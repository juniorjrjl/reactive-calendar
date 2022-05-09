package br.com.study.reactivecalendar.api.mapper;

import br.com.study.reactivecalendar.api.controller.request.AppointmentRequest;
import br.com.study.reactivecalendar.api.controller.response.AppointmentSingleResponse;
import br.com.study.reactivecalendar.api.controller.response.GuestSingleResponse;
import br.com.study.reactivecalendar.domain.document.AppointmentDocument;
import br.com.study.reactivecalendar.domain.dto.AppointmentDTO;
import br.com.study.reactivecalendar.domain.dto.GuestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface AppointmentControllerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "guests", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AppointmentDocument toDocument(final AppointmentRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AppointmentDTO toDTO(final AppointmentRequest request);

    @Mapping(target = "guests", expression = "java(toResponse(document.guests()))")
    AppointmentSingleResponse toResponse(final AppointmentDTO document);

    @Mapping(target = "email", source = "email")
    Set<GuestSingleResponse> toResponse(final Set<GuestDTO> document);

}
