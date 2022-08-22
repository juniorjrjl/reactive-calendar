package br.com.study.reactivecalendar.core.factoryBot.request;

import br.com.study.reactivecalendar.api.controller.request.AppointmentRequest;
import br.com.study.reactivecalendar.api.controller.request.GuestRequest;
import br.com.study.reactivecalendar.domain.document.GuestType;
import com.github.javafaker.Faker;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.study.reactivecalendar.core.RandomData.between;
import static br.com.study.reactivecalendar.core.RandomData.birthday;
import static br.com.study.reactivecalendar.core.RandomData.getFaker;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class AppointmentRequestFactoryBot {

    public static AppointmentRequestFactoryBotBuilder builder(){
        return new AppointmentRequestFactoryBotBuilder();
    }

    public static class AppointmentRequestFactoryBotBuilder{

        private String title;
        private String details;
        private OffsetDateTime startIn;
        private OffsetDateTime endIn;
        private Set<GuestRequest> guests;
        private final Faker faker = getFaker();

        public AppointmentRequestFactoryBotBuilder() {
            this.title = faker.lorem().sentence(100);
            this.details = faker.lorem().sentence(255);
            this.startIn = birthday();
            this.endIn = between(startIn, OffsetDateTime.now().plusYears(80L));
            this.guests = generateGuests(faker.number().randomDigitNotZero(), 1);
        }

        public AppointmentRequestFactoryBotBuilder blankTitle(){
            this.title = faker.bool().bool() ? null : "   ";
            return this;
        }

        public AppointmentRequestFactoryBotBuilder blankDetails(){
            this.details = faker.bool().bool() ? null : "   ";
            return this;
        }

        public AppointmentRequestFactoryBotBuilder withoutGuests(){
            this.guests = faker.bool().bool() ? null : new HashSet<>();
            return this;
        }

        public AppointmentRequestFactoryBotBuilder withoutStartIn(){
            this.startIn = null;
            return this;
        }

        public AppointmentRequestFactoryBotBuilder withoutEndIn(){
            this.endIn = null;
            return this;
        }

        public AppointmentRequestFactoryBotBuilder invalidPeriod(){
            this.endIn = birthday();
            this.startIn = between(endIn, OffsetDateTime.now().plusYears(80L));
            return this;
        }

        public AppointmentRequestFactoryBotBuilder withoutGuestAdmin(){
            this.guests = generateGuests(faker.number().randomDigitNotZero(), 0);
            return this;
        }

        private Set<GuestRequest> generateGuests(final int amount, final int admin){
            var guests = Stream.generate(() ->  GuestRequest.builder().email(faker.internet().emailAddress())
                            .type(GuestType.STANDARD)
                            .build())
                    .limit(amount)
                    .collect(Collectors.toSet());
            guests.addAll(Stream.generate(() -> GuestRequest.builder().email(faker.internet().emailAddress())
                    .type(GuestType.ADMIN)
                    .build())
                    .limit(admin)
                    .toList());
            return guests;
        }

        public AppointmentRequest build(){
            return AppointmentRequest.builder()
                    .title(title)
                    .details(details)
                    .startIn(startIn)
                    .endIn(endIn)
                    .guests(guests)
                    .build();
        }

    }

}
