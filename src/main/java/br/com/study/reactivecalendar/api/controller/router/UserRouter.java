package br.com.study.reactivecalendar.api.controller.router;

import br.com.study.reactivecalendar.api.controller.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static br.com.study.reactivecalendar.api.controller.requestpredicate.UserRequestPredicate.deletePredicate;
import static br.com.study.reactivecalendar.api.controller.requestpredicate.UserRequestPredicate.findByIdPredicate;
import static br.com.study.reactivecalendar.api.controller.requestpredicate.UserRequestPredicate.savePredicate;
import static br.com.study.reactivecalendar.api.controller.requestpredicate.UserRequestPredicate.updatePredicate;

@Configuration
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> userRoute(final UserHandler handler){
        return RouterFunctions.route(findByIdPredicate(), handler::findById)
                .andRoute(updatePredicate(), handler::update)
                .andRoute(deletePredicate(), handler::delete)
                .andRoute(savePredicate(), handler::save);
    }

}
