package br.com.study.reactivecalendar.core.factoryBot.document;

import br.com.study.reactivecalendar.domain.document.UserDocument;
import com.github.javafaker.Faker;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.OffsetDateTime;

import static br.com.study.reactivecalendar.core.RandomData.getFaker;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UserDocumentFactoryBot {

    public static UserDocumentFactoryBotBuilder builder(){
        return new UserDocumentFactoryBotBuilder();
    }

    public static class UserDocumentFactoryBotBuilder{

        private String id;
        private String name;
        private String email;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private final Faker faker = getFaker();

        public UserDocumentFactoryBotBuilder() {
            this.id = ObjectId.get().toString();
            this.name = faker.name().firstName();
            this.email = faker.internet().emailAddress();
            this.createdAt = OffsetDateTime.now();
            this.updatedAt = OffsetDateTime.now();
        }

        public UserDocumentFactoryBotBuilder preInsert(){
            this.id = null;
            this.createdAt = null;
            this.updatedAt = null;
            return this;
        }

        public UserDocument build(){
            return new UserDocument(id, name, email, createdAt, updatedAt);
        }

    }

}
