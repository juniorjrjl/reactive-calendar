package br.com.study.reactivecalendar.domain.service;

import br.com.study.reactivecalendar.domain.document.UserDocument;
import br.com.study.reactivecalendar.domain.repository.UserRepository;
import br.com.study.reactivecalendar.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserQueryService userQueryService;
    private final UserRepository userRepository;

    public Mono<UserDocument> save(final UserDocument document){
        return userRepository.save(document)
                .doFirst(() -> log.info("Try to persist a follow document: {}", document));
    }

    public Mono<UserDocument> update(final UserDocument document){
        return userQueryService.findById(document.id())
                .map(deck -> document.toBuilder()
                        .createdAt(deck.createdAt())
                        .build())
                .flatMap(userRepository::save)
                .doFirst(() -> log.info("try to update a follow user {}", document));
    }

    public Mono<Void> delete(final String id){
        return userQueryService.findById(id)
                .flatMap(userRepository::delete)
                .doFirst(() -> log.info("try to delete user with id {}", id));
    }

}
