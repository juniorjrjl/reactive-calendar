package br.com.study.reactivecalendar.api.controller.request;

import org.apache.commons.lang3.ObjectUtils;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

public record AppointmentUpdateRequest(
        String title,
        String details,
        OffsetDateTime startIn,
        OffsetDateTime endIn,
        Set<GuestRequest> newGuests,
        Set<String> guestsToRemove
) {

    public AppointmentUpdateRequest{
        newGuests = ObjectUtils.defaultIfNull(newGuests, new HashSet<>());
        guestsToRemove = ObjectUtils.defaultIfNull(guestsToRemove, new HashSet<>());
    }

}
