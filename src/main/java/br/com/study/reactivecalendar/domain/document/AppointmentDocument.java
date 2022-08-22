package br.com.study.reactivecalendar.domain.document;

import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.Set;

@Document(collection = "appointments")
public record AppointmentDocument(

    @Id
    String id,
    String title,
    String details,
    OffsetDateTime startIn,
    OffsetDateTime endIn,
    Set<Guest> guests,
    @CreatedDate
    OffsetDateTime createdAt,
    @LastModifiedDate
    OffsetDateTime updatedAt){

    @Builder(toBuilder = true)
    public AppointmentDocument {}

}
