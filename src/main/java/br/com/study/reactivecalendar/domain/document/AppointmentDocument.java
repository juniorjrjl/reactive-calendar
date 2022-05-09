package br.com.study.reactivecalendar.domain.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDocument {

    @Id
    private String id;
    private String title;
    private String details;
    private OffsetDateTime startIn;
    private OffsetDateTime endIn;
    private Set<Guest> guests = new HashSet<>();
    @CreatedDate
    private OffsetDateTime createdAt;
    @LastModifiedDate
    private OffsetDateTime updatedAt;

}
