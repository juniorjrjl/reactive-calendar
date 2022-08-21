package br.com.study.reactivecalendar.utils.request;

import br.com.study.reactivecalendar.api.controller.response.ProblemResponse;
import br.com.study.reactivecalendar.api.controller.response.UserSingleResponse;
import org.springframework.web.reactive.function.server.RouterFunction;

public class RequestBuilderInstances {

    public static RequestBuilder<UserSingleResponse> userResponseRequestBuilder(final RouterFunction<?> routerFunction, final String baseUri){
        return new RequestBuilder<>(routerFunction, baseUri, UserSingleResponse.class);
    }

    public static RequestBuilder<ProblemResponse> problemResponseRequestBuilder(final RouterFunction<?> routerFunction, final String baseUri){
        return new RequestBuilder<>(routerFunction, baseUri, ProblemResponse.class);
    }

    public static RequestBuilder<Void> noBodyResponseRequestBuilder(final RouterFunction<?> routerFunction, final String baseUri){
        return new RequestBuilder<>(routerFunction, baseUri, Void.class);
    }

}
