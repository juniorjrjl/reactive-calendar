package br.com.study.reactivecalendar.domain.repository;

import br.com.study.reactivecalendar.domain.document.UserDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserDocument, String> {

    Mono<UserDocument> findFirstByEmail(final String email);

}
