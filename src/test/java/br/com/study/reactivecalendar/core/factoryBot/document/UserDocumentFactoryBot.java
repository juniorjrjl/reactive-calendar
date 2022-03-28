package br.com.study.reactivecalendar.core.factoryBot.document;

import br.com.study.reactivecalendar.core.RandomData;
import br.com.study.reactivecalendar.domain.document.UserDocument;
import com.github.javafaker.Faker;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.OffsetDateTime;

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
        private final Faker faker = RandomData.getFaker();

        public UserDocumentFactoryBotBuilder() {
            this.id = ObjectId.get().toString();
            this.name = faker.name().firstName();
            this.email = faker.internet().emailAddress();
            this.createdAt = OffsetDateTime.now();
            this.updatedAt = OffsetDateTime.now();
        }

        public UserDocument build(){
            var userDocument = new UserDocument();
            userDocument.setId(id);
            userDocument.setName(name);
            userDocument.setEmail(email);
            userDocument.setCreatedAt(createdAt);
            userDocument.setUpdatedAt(updatedAt);
            return userDocument;
        }

    }

}
