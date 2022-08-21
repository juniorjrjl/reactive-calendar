package br.com.study.reactivecalendar.api.controller.handler;

import br.com.study.reactivecalendar.api.controller.request.UserIdParam;
import br.com.study.reactivecalendar.api.controller.request.UserRequest;
import br.com.study.reactivecalendar.api.controller.response.UserSingleResponse;
import br.com.study.reactivecalendar.api.mapper.UserMapper;
import br.com.study.reactivecalendar.domain.service.BeanValidationService;
import br.com.study.reactivecalendar.domain.service.UserService;
import br.com.study.reactivecalendar.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@AllArgsConstructor
@Slf4j
public class UserHandler {

    private final BeanValidationService beanValidationService;
    private final UserService userService;
    private final UserQueryService userQueryService;
    private final UserMapper userMapper;
    public Mono<ServerResponse> findById(final ServerRequest request){
        return getIdParam(request)
                .flatMap(userQueryService::findById)
                .map(userMapper::toResponse)
                .flatMap(response -> ok()
                        .contentType(APPLICATION_JSON)
                        .body(Mono.just(response), UserSingleResponse.class));
    }

    public Mono<ServerResponse> save(final ServerRequest request){
        return request.bodyToMono(UserRequest.class)
                .flatMap(user -> beanValidationService.verifyConstraints(user, "userRequest").thenReturn(user))
                .flatMap(user -> userService.save(userMapper.toDocument(user)))
                .map(userMapper::toResponse)
                .flatMap(response -> created(UriComponentsBuilder.fromPath("/users")
                        .pathSegment("{id}")
                        .build(response.id()))
                        .contentType(APPLICATION_JSON)
                        .body(Mono.just(response), UserSingleResponse.class));
    }

    public Mono<ServerResponse> update(final ServerRequest request){
        return request.bodyToMono(UserRequest.class)
                .flatMap(user -> beanValidationService.verifyConstraints(user, "userRequest").thenReturn(user))
                .zipWhen(user -> getIdParam(request))
                .flatMap(tuple -> userService.update(userMapper.toDocument(tuple.getT2(), tuple.getT1())))
                .map(userMapper::toResponse)
                .flatMap(response -> ok()
                        .contentType(APPLICATION_JSON)
                        .body(Mono.just(response), UserSingleResponse.class));
    }

    public Mono<ServerResponse> delete(final ServerRequest request){
        return getIdParam(request)
                .flatMap(userService::delete)
                .then(noContent().build());
    }

    private Mono<String> getIdParam(final ServerRequest request){
        return Mono.just(new UserIdParam(request.pathVariable("id")))
                .flatMap(param -> beanValidationService.verifyConstraints(param, "userIdParam"))
                .thenReturn(request.pathVariable("id"));
    }

}
