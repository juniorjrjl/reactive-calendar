package br.com.study.reactivecalendar.core.factoryBot.document;

import br.com.study.reactivecalendar.domain.document.AppointmentDocument;
import br.com.study.reactivecalendar.domain.document.Guest;
import br.com.study.reactivecalendar.domain.document.GuestType;
import com.github.javafaker.Faker;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.study.reactivecalendar.core.RandomData.between;
import static br.com.study.reactivecalendar.core.RandomData.birthday;
import static br.com.study.reactivecalendar.core.RandomData.getFaker;
import static br.com.study.reactivecalendar.core.RandomData.randomEnum;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class AppointmentDocumentFactoryBot {

    public static AppointmentDocumentFactoryBotBuilder builder(){
        return new AppointmentDocumentFactoryBotBuilder();
    }

    public static class AppointmentDocumentFactoryBotBuilder{

        private final String id;
        private final String title;
        private final String details;
        private final OffsetDateTime startIn;
        private final OffsetDateTime endIn;
        private final Set<Guest> guests;
        private final OffsetDateTime createdAt;
        private final OffsetDateTime updatedAt;
        private final Faker faker = getFaker();

        public AppointmentDocumentFactoryBotBuilder() {
            this.id = ObjectId.get().toString();
            this.title = faker.lorem().sentence(100);
            this.details = faker.lorem().sentence(255);
            this.startIn = birthday();
            this.endIn = between(startIn, OffsetDateTime.now().plusYears(80L));
            this.guests = generateGuests(faker.number().randomDigitNotZero());
            this.createdAt = OffsetDateTime.now();
            this.updatedAt = OffsetDateTime.now();
        }

        private Set<Guest> generateGuests(final int amount){
            return Stream.generate(() ->  Guest.builder().userId(ObjectId.get().toString())
                            .type(randomEnum(GuestType.class))
                            .build())
                    .limit(amount)
                    .collect(Collectors.toSet());
        }

        public AppointmentDocument build(){
            return AppointmentDocument.builder()
                    .id(id)
                    .title(title)
                    .details(details)
                    .startIn(startIn)
                    .endIn(endIn)
                    .guests(guests)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .build();
        }

    }

}
