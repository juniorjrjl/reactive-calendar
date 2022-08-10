package br.com.study.reactivecalendar.domain.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public record AppointmentDTO(String id,
                             String title,
                             String details,
                             OffsetDateTime startIn,
                             OffsetDateTime endIn,
                             Set<GuestDTO>guests,
                             OffsetDateTime createdAt,
                             OffsetDateTime updatedAt) {

    public static AppointmentDTOBuilder builder(){
        return new AppointmentDTOBuilder();
    }

    public AppointmentDTOBuilder toBuilder(){
        return new AppointmentDTOBuilder(id, title, details, startIn, endIn, guests, createdAt, updatedAt);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppointmentDTOBuilder{

        private String id;
        private String title;
        private String details;
        private OffsetDateTime startIn;
        private OffsetDateTime endIn;
        private Set<GuestDTO>guests;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        public AppointmentDTOBuilder id(final String id){
            this.id = id;
            return this;
        }

        public AppointmentDTOBuilder title(final String title){
            this.title = title;
            return this;
        }

        public AppointmentDTOBuilder details(final String details){
            this.details = details;
            return this;
        }

        public AppointmentDTOBuilder startIn(final OffsetDateTime startIn){
            this.startIn = startIn;
            return this;
        }
        public AppointmentDTOBuilder endIn(final OffsetDateTime endIn){
            this.endIn = endIn;
            return this;
        }

        public AppointmentDTOBuilder guests(final Set<GuestDTO> guests){
            this.guests = guests;
            return this;
        }

        public AppointmentDTOBuilder addGuests(final GuestDTO... guests){
            this.guests = new HashSet<>(this.guests);
            this.guests.addAll(Arrays.asList(guests));
            return this;
        }

        public AppointmentDTOBuilder createdAt(final OffsetDateTime createdAt){
            this.createdAt = createdAt;
            return this;
        }

        public AppointmentDTOBuilder updatedAt(final OffsetDateTime updatedAt){
            this.updatedAt = updatedAt;
            return this;
        }

        public AppointmentDTO build(){
            return new AppointmentDTO(id, title, details, startIn, endIn, guests, createdAt, updatedAt);
        }

    }

}
