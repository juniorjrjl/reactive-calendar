package br.com.study.reactivecalendar.core.factoryBot.request;

import br.com.study.reactivecalendar.api.controller.request.AppointmentIdParam;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import static br.com.study.reactivecalendar.core.RandomData.getFaker;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class AppointmentIdParamFactoryBot {

    public static AppointmentIdParamFactoryBotBuilder builder(){
        return new AppointmentIdParamFactoryBotBuilder();
    }

    public static class AppointmentIdParamFactoryBotBuilder{

        public String id;

        public AppointmentIdParamFactoryBotBuilder(){
            this.id = ObjectId.get().toString();
        }

        public AppointmentIdParamFactoryBotBuilder withInvalidId(){
            final var faker = getFaker();
            this.id = faker.bool().bool() ? null : faker.lorem().word();
            return this;
        }

        public AppointmentIdParam build(){
            return new AppointmentIdParam(this.id);
        }

    }

}
