package br.com.study.reactivecalendar.core.factoryBot.request;

import br.com.study.reactivecalendar.api.controller.request.AppointmentUpdateRequest;
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
public class AppointmentUpdateRequestFactoryBot {

    public static AppointmentUpdateRequestFactoryBotBuilder builder(){
        return new AppointmentUpdateRequestFactoryBotBuilder();
    }

    public static class AppointmentUpdateRequestFactoryBotBuilder{

        private String title;
        private String details;
        private OffsetDateTime startIn;
        private OffsetDateTime endIn;
        private final Set<GuestRequest> newGuests;
        private final Set<String> guestsToRemove = new HashSet<>();
        private final Faker faker = getFaker();

        public AppointmentUpdateRequestFactoryBotBuilder() {
            this.title = faker.lorem().characters(100);
            this.details = faker.lorem().characters(255);
            this.startIn = birthday();
            this.endIn = between(startIn, OffsetDateTime.now().plusYears(80L));
            this.newGuests = generateGuests(faker.number().randomDigitNotZero(), faker.number().randomDigitNotZero());
            this.guestsToRemove.addAll(Stream.generate(() -> faker.internet().emailAddress())
                    .limit(faker.number().randomDigitNotZero())
                    .collect(Collectors.toSet()));
        }

        public AppointmentUpdateRequestFactoryBotBuilder blankTitle(){
            this.title = faker.bool().bool() ? null : "   ";
            return this;
        }

        public AppointmentUpdateRequestFactoryBotBuilder blankDetails(){
            this.details = faker.bool().bool() ? null : "   ";
            return this;
        }

        public AppointmentUpdateRequestFactoryBotBuilder withoutStartIn(){
            this.startIn = null;
            return this;
        }

        public AppointmentUpdateRequestFactoryBotBuilder withoutEndIn(){
            this.endIn = null;
            return this;
        }

        public AppointmentUpdateRequestFactoryBotBuilder invalidPeriod(){
            this.endIn = birthday();
            this.startIn = between(endIn, OffsetDateTime.now().plusYears(80L));
            return this;
        }

        public AppointmentUpdateRequestFactoryBotBuilder invalidEmailInGuestToRemove(){
            this.guestsToRemove.add(faker.lorem().word());
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

        public AppointmentUpdateRequest build(){
            return AppointmentUpdateRequest.builder()
                    .title(title)
                    .details(details)
                    .startIn(startIn)
                    .endIn(endIn)
                    .newGuests(newGuests)
                    .guestsToRemove(guestsToRemove)
                    .build();
        }

    }

}
