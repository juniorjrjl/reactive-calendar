package br.com.study.reactivecalendar.api.controller;

import br.com.study.reactivecalendar.api.controller.documentation.UserControllerDocumentation;
import br.com.study.reactivecalendar.api.controller.request.UserRequest;
import br.com.study.reactivecalendar.api.controller.response.UserSingleResponse;
import br.com.study.reactivecalendar.api.mapper.UserMapper;
import br.com.study.reactivecalendar.core.validation.MongoId;
import br.com.study.reactivecalendar.domain.service.UserService;
import br.com.study.reactivecalendar.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController implements UserControllerDocumentation {

    private final UserService userService;
    private final UserQueryService userQueryService;
    private final UserMapper userMapper;

    @Override
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<UserSingleResponse> save(@RequestBody @Valid final UserRequest request){
        return userService.save(userMapper.toDocument(request))
                .doFirst(() -> log.info("try to insert user with follow request {}", request))
                .map(userMapper::toResponse);
    }

    @Override
    @PutMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Mono<UserSingleResponse> update(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id,
                                            @RequestBody @Valid final UserRequest request){
        return userService.update(userMapper.toDocument(id, request))
                .doFirst(() -> log.info("try to update user {} with follow request {}", id, request))
                .map(userMapper::toResponse);
    }

    @Override
    @DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> delete(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id){
        return userService.delete(id)
                .doFirst(() -> log.info("try to delete a user with follow id {}", id));
    }

    @Override
    @GetMapping(value = "/{id}")
    @ResponseStatus(OK)
    public Mono<UserSingleResponse> findById(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id){
        return userQueryService.findById(id)
                .map(userMapper::toResponse);
    }

}
