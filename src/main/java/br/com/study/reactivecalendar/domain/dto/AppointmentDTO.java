package br.com.study.reactivecalendar.domain.dto;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Set;

public record AppointmentDTO(String id,
                             String title,
                             String details,
                             OffsetDateTime startIn,
                             OffsetDateTime endIn,
                             Set<GuestDTO>guests,
                             OffsetDateTime createdAt,
                             OffsetDateTime updatedAt) {

    @Builder(toBuilder = true)
    public AppointmentDTO {}

}
