package br.com.study.reactivecalendar.core.factoryBot.dto;

import br.com.study.reactivecalendar.core.RandomData;
import br.com.study.reactivecalendar.domain.document.GuestType;
import br.com.study.reactivecalendar.domain.dto.GuestDTO;
import com.github.javafaker.Faker;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import static br.com.study.reactivecalendar.core.RandomData.randomEnum;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class GuestDTOFactoryBot {

    public static GuestDTOFactoryBotBuilder builder(){
        return new GuestDTOFactoryBotBuilder();
    }

    public static class GuestDTOFactoryBotBuilder{

        private String userId;
        private String email;
        private GuestType type;

        private final Faker faker = RandomData.getFaker();

        public GuestDTOFactoryBotBuilder(){
            this.userId = ObjectId.get().toString();
            this.email = faker.internet().emailAddress();
            this.type = randomEnum(GuestType.class);
        }

        public GuestDTO build(){
            return GuestDTO.builder()
                    .userId(this.userId)
                    .email(this.email)
                    .type(this.type)
                    .build();
        }

    }

}
