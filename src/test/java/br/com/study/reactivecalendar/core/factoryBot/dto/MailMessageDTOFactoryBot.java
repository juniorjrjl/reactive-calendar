package br.com.study.reactivecalendar.core.factoryBot.dto;

import br.com.study.reactivecalendar.domain.dto.MailMessageDTO;
import com.github.javafaker.Faker;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;

import static br.com.study.reactivecalendar.core.RandomData.between;
import static br.com.study.reactivecalendar.core.RandomData.birthday;
import static br.com.study.reactivecalendar.core.RandomData.getFaker;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class MailMessageDTOFactoryBot {

    public static MailMessageDTOFactoryBotBuilder builder(){
        return new MailMessageDTOFactoryBotBuilder();
    }

    public static class MailMessageDTOFactoryBotBuilder{

        private final Faker faker = getFaker();
        private final List<String> destinations;
        private final String title;
        private final String details;
        private final OffsetDateTime startIn;
        private final OffsetDateTime endIn;
        private final String owner;

        public MailMessageDTOFactoryBotBuilder() {
            this.destinations = Stream.generate(() -> faker.internet().emailAddress()).limit(faker.number().randomDigitNotZero()).toList();
            this.title = faker.lorem().word();
            this.details = faker.lorem().word();
            this.startIn = birthday();
            this.endIn = between(startIn, startIn.plusHours(10L));
            this.owner = faker.internet().emailAddress();
        }

        public MailMessageDTO buildNewAppointment() {
            return MailMessageDTO.buildNewAppointment()
                    .destinations(destinations.toArray(new String[0]))
                    .title(title)
                    .details(details)
                    .startIn(startIn)
                    .endIn(endIn)
                    .owner(owner)
                    .build();
        }

        public MailMessageDTO buildEditAppointment(){
            return MailMessageDTO.buildEditAppointment()
                    .destinations(destinations.toArray(new String[0]))
                    .title(title)
                    .details(details)
                    .startIn(startIn)
                    .endIn(endIn)
                    .owner(owner)
                    .build();
        }

        public MailMessageDTO buildCancelAppointment(){
            return MailMessageDTO.buildCancelAppointment()
                    .destinations(destinations.toArray(new String[0]))
                    .title(title)
                    .details(details)
                    .startIn(startIn)
                    .endIn(endIn)
                    .owner(owner)
                    .build();
        }

        public MailMessageDTO buildRemoveAppointment(){
            return MailMessageDTO.buildRemoveAppointment()
                    .destinations(destinations.toArray(new String[0]))
                    .title(title)
                    .details(details)
                    .startIn(startIn)
                    .endIn(endIn)
                    .owner(owner)
                    .build();
        }

    }

}
