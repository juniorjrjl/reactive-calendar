package br.com.study.reactivecalendar.domain.service.query;

import br.com.study.reactivecalendar.domain.document.UserDocument;
import br.com.study.reactivecalendar.domain.exception.NotFoundException;
import br.com.study.reactivecalendar.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static br.com.study.reactivecalendar.domain.exception.BaseErrorMessage.USER_NOT_FOUND_EXCEPTION;

@Service
@AllArgsConstructor
@Slf4j
public class UserQueryService {

    private final UserRepository userRepository;

    public Mono<UserDocument> findById(final String id){
        return userRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(USER_NOT_FOUND_EXCEPTION.params(id).getMessage()))))
                .doFirst(() -> log.info("Try to find user with id {}", id));
    }

}
