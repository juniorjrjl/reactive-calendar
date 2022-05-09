package br.com.study.reactivecalendar.domain.repository;

import br.com.study.reactivecalendar.domain.document.AppointmentDocument;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.OffsetDateTime;

@Repository
public interface AppointmentRepository extends ReactiveMongoRepository<AppointmentDocument, String> {


    @Query("""
           {
               "guests.userId" : {
                   $eq: ?0
               },
               $and: [
               　　　　　　　　{
                           　　　　　$or: [
                           　　　　　　　　　{"startIn": { $gte: ?1 }},
                       　　　　　　　　　　　{"endIn": { $lte: ?2 }}
                   　　　　　　　　　　]
               　　　　　　　　}
               ]
           }
           """)
    Flux<AppointmentDocument> findUserAppointmentsInInterval(final String userId,
                                                             final OffsetDateTime startIn,
                                                             final OffsetDateTime endIn);

}
