package br.com.study.reactivecalendar.domain;

import br.com.study.reactivecalendar.domain.document.UserDocument;
import br.com.study.reactivecalendar.domain.dto.AppointmentDTO;
import br.com.study.reactivecalendar.domain.dto.GuestDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

public record AppointmentPreUpdate(AppointmentDTO appointmentDTO,
                                   Set<GuestDTO> newGuest,
                                   Set<UserDocument> guestsToRemove) {

    public static AppointmentPreUpdateBuilder builder(){
        return new AppointmentPreUpdateBuilder();
    }

    public AppointmentPreUpdateBuilder toBuilder(){
        return new AppointmentPreUpdateBuilder(appointmentDTO, newGuest, guestsToRemove);
    }

    public Set<String> getGuestsEmailToRemove(){
        return guestsToRemove.stream().map(UserDocument::email).collect(Collectors.toSet());
    }

    public Set<String> getGuestsAlreadyInAppointment(){
        return appointmentDTO.guests().stream().map(GuestDTO::email)
                .filter(email -> !getGuestsEmailToRemove().contains(email)).collect(Collectors.toSet());
    }

    public Set<String> getNewGuestsEmail(){
        return newGuest.stream().map(GuestDTO::email).collect(Collectors.toSet());
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppointmentPreUpdateBuilder{

        private AppointmentDTO appointmentDTO;
        private Set<GuestDTO> newGuest;
        private Set<UserDocument> guestsToRemove;

        public AppointmentPreUpdateBuilder addNewGuests(){
            this.appointmentDTO.toBuilder().addGuests(newGuest.toArray(new GuestDTO[0])).build();
            return this;
        }

        public AppointmentPreUpdateBuilder appointmentDTO(final AppointmentDTO appointmentDTO){
            this.appointmentDTO = appointmentDTO;
            return this;
        }

        public AppointmentPreUpdateBuilder newGuest(final Set<GuestDTO> newGuest){
            this.newGuest = newGuest;
            return this;
        }

        public AppointmentPreUpdateBuilder guestsToRemove(final Set<UserDocument> guestsToRemove){
            this.guestsToRemove = guestsToRemove;
            return this;
        }

        public AppointmentPreUpdate build(){
            return new AppointmentPreUpdate(appointmentDTO, newGuest, guestsToRemove);
        }

    }

}

