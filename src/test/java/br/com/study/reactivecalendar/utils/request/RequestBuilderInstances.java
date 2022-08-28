package br.com.study.reactivecalendar.utils.request;

import br.com.study.reactivecalendar.api.controller.response.ProblemResponse;
import br.com.study.reactivecalendar.api.controller.response.UserSingleResponse;
import org.springframework.context.ApplicationContext;

public class RequestBuilderInstances {

    public static RequestBuilder<UserSingleResponse> userResponseRequestBuilder(final ApplicationContext applicationContext, final String baseUri){
        return new RequestBuilder<>(applicationContext, baseUri, UserSingleResponse.class);
    }

    public static RequestBuilder<ProblemResponse> problemResponseRequestBuilder(final ApplicationContext applicationContext, final String baseUri){
        return new RequestBuilder<>(applicationContext, baseUri, ProblemResponse.class);
    }

    public static RequestBuilder<Void> noBodyResponseRequestBuilder(final ApplicationContext applicationContext, final String baseUri){
        return new RequestBuilder<>(applicationContext, baseUri, Void.class);
    }

}
