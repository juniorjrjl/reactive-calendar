package br.com.study.reactivecalendar.api.controller.requestpredicate;

import org.springframework.web.reactive.function.server.RequestPredicate;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

public class UserRequestPredicate {

    private static final String BASE_URI = "users";
    private static final String ID_PARAM = "{id}";

    public static RequestPredicate findByIdPredicate(){
        return GET(String.format("%s/%s", BASE_URI, ID_PARAM));
    }

    public static RequestPredicate savePredicate(){
        return POST(String.format("%s/", BASE_URI)).and(accept(APPLICATION_JSON));
    }

    public static RequestPredicate updatePredicate(){
        return PUT(String.format("%s/%s", BASE_URI, ID_PARAM)).and(accept(APPLICATION_JSON));
    }

    public static RequestPredicate deletePredicate(){
        return DELETE(String.format("%s/%s", BASE_URI, ID_PARAM));
    }

}
