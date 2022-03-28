package br.com.study.reactivecalendar.core.factoryBot.request;

import br.com.study.reactivecalendar.api.controller.request.UserRequest;
import br.com.study.reactivecalendar.core.RandomData;
import com.github.javafaker.Faker;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UserRequestFactoryBot {

    public static UserRequestFactoryBotBuilder builder(){
        return new UserRequestFactoryBotBuilder();
    }

    public static class UserRequestFactoryBotBuilder{

        private String name;
        private String email;
        private final Faker faker = RandomData.getFaker();

        public UserRequestFactoryBotBuilder() {
            this.name = faker.name().firstName();
            this.email = faker.internet().emailAddress();
        }

        public UserRequestFactoryBotBuilder withBlankName(){
            this.name = faker.bool().bool() ? null : "     ";
            return this;
        }

        public UserRequestFactoryBotBuilder withLargeName(){
            this.name = faker.lorem().characters(256, 500);
            return this;
        }

        public UserRequestFactoryBotBuilder withBlankEmail(){
            this.email = faker.bool().bool() ? null : "     ";
            return this;
        }

        public UserRequestFactoryBotBuilder withLargeEmail(){
            this.email = faker.lorem().characters(256, 500);
            return this;
        }

        public UserRequestFactoryBotBuilder withInvalidEmail(){
            this.email = faker.lorem().word();
            return this;
        }

        public UserRequest build(){
            return UserRequest.builder()
                    .name(name)
                    .email(email)
                    .build();
        }

    }

}
