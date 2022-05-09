package br.com.study.reactivecalendar.domain.document;

import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Document(collection = "users")
public record UserDocument(
        @Id
        String id,
        String name,
        String email,
        @CreatedDate
        OffsetDateTime createdAt,
        @LastModifiedDate
        OffsetDateTime updatedAt){

        @Builder(toBuilder = true)
        public UserDocument {}


}
