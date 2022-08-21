package br.com.study.reactivecalendar.api.controller.router;

import br.com.study.reactivecalendar.api.controller.handler.AppointmentHandler;
import br.com.study.reactivecalendar.api.controller.router.documentation.IAppointmentRouterDoc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static br.com.study.reactivecalendar.api.controller.requestpredicate.AppointmentRequestPredicate.deletePredicate;
import static br.com.study.reactivecalendar.api.controller.requestpredicate.AppointmentRequestPredicate.findByIdPredicate;
import static br.com.study.reactivecalendar.api.controller.requestpredicate.AppointmentRequestPredicate.savePredicate;
import static br.com.study.reactivecalendar.api.controller.requestpredicate.AppointmentRequestPredicate.updatePredicate;

@Configuration
public class AppointmentRouter implements IAppointmentRouterDoc {

    @Override
    @Bean
    public RouterFunction<ServerResponse> appointmentRoute(final AppointmentHandler handler){
        return RouterFunctions.route(findByIdPredicate(), handler::findById)
                .andRoute(updatePredicate(), handler::update)
                .andRoute(deletePredicate(), handler::delete)
                .andRoute(savePredicate(), handler::save);
    }

}
