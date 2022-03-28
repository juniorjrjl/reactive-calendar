package br.com.study.reactivecalendar.utils.request;

import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class RequestBuilder<B> {

    private final WebTestClient webTestClient;
    private Function<UriBuilder, URI> uriFunction;
    private final Map<String, Set<String>> headers = new HashMap<>();
    private Object body;
    private final Class<B> responseClass;

    public RequestBuilder(final ApplicationContext applicationContext, final String baseUri, final Class<B> responseClass){
        this.responseClass = responseClass;
        webTestClient = WebTestClient
                .bindToApplicationContext(applicationContext)
                .configureClient()
                .baseUrl(baseUri)
                .responseTimeout(Duration.ofDays(1))
                .build();
    }

    public RequestBuilder<B> withUri(final Function<UriBuilder, URI> uriFunction){
        this.uriFunction = uriFunction;
        return this;
    }

    public RequestBuilder<B> withBody(final Object body){
        this.body = body;
        return this;
    }

    public RequestBuilder<B> withJWTAuthorization(final String token){
        headers.put(AUTHORIZATION, Set.of(token));
        return this;
    }

    public RequestBuilder<B> withHeaders(final Map<String, Set<String>> headers){
        this.headers.putAll(headers);
        return this;
    }

    public RequestBuilder<B> withHeader(final String key, final Set<String> value){
        this.headers.put(key, value);
        return this;
    }

    public SimpleRequestBuilder<B> generateRequestWithSimpleBody(){
        if (Objects.isNull(uriFunction)){
            throw new IllegalArgumentException("Informe a uri do recurso a ser consumido");
        }
        return new SimpleRequestBuilder<>(webTestClient, uriFunction, headers, body, responseClass);
    }

    public NoBodyRequestBuilder generateRequestWithoutBody(){
        if (Objects.isNull(uriFunction)){
            throw new IllegalArgumentException("Informe a uri do recurso a ser consumido");
        }
        if (responseClass != Void.class){
            throw new IllegalArgumentException("Use a classe Void para requisições sem response body");
        }
        return new NoBodyRequestBuilder(webTestClient, uriFunction, headers, body);
    }

    public CollectionRequestBuilder<B> generateRequestWithCollectionBody(){
        if (Objects.isNull(uriFunction)){
            throw new IllegalArgumentException("Informe a uri do recurso a ser consumido");
        }
        if (responseClass == Void.class){
            throw new IllegalArgumentException("Use a classe Void para requisições sem response body");
        }
        return new CollectionRequestBuilder<B>(webTestClient, uriFunction, headers, body, responseClass);
    }

}
