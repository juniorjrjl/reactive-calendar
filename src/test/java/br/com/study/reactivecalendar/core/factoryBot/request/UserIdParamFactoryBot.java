package br.com.study.reactivecalendar.core.factoryBot.request;

import br.com.study.reactivecalendar.api.controller.request.UserIdParam;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import static br.com.study.reactivecalendar.core.RandomData.getFaker;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UserIdParamFactoryBot {

    public static UserIdParamFactoryBotBuilder builder(){
        return new UserIdParamFactoryBotBuilder();
    }

    public static class UserIdParamFactoryBotBuilder{

        public String id;

        public UserIdParamFactoryBotBuilder(){
            this.id = ObjectId.get().toString();
        }

        public UserIdParamFactoryBotBuilder withInvalidId(){
            final var faker = getFaker();
            this.id = faker.bool().bool() ? null : faker.lorem().word();
            return this;
        }

        public UserIdParam build(){
            return new UserIdParam(this.id);
        }

    }

}
