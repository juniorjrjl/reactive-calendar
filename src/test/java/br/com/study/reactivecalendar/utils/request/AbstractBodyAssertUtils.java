package br.com.study.reactivecalendar.utils.request;

import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.function.Consumer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

public class AbstractBodyAssertUtils<B> {

    private final EntityExchangeResult<B> response;

    public AbstractBodyAssertUtils(final EntityExchangeResult<B> response) {
        this.response = response;
    }

    public B getBody(){
        return response.getResponseBody();
    }

    public AbstractBodyAssertUtils<B> assertBody(Consumer<B> consumer){
        consumer.accept(response.getResponseBody());
        return this;
    }

    public AbstractBodyAssertUtils<B> isHttpStatusIsCreated(){
        assertThat(response.getStatus()).isEqualTo(CREATED);
        return this;
    }

    public AbstractBodyAssertUtils<B> isHttpStatusIsOk(){
        assertThat(response.getStatus()).isEqualTo(OK);
        return this;
    }

    public AbstractBodyAssertUtils<B> isHttpStatusIsNotFound(){
        assertThat(response.getStatus()).isEqualTo(NOT_FOUND);
        return this;
    }

    public AbstractBodyAssertUtils<B> isHttpStatusIsAccepted(){
        assertThat(response.getStatus()).isEqualTo(ACCEPTED);
        return this;
    }

    public AbstractBodyAssertUtils<B> isHttpStatusIsNoContent(){
        assertThat(response.getStatus()).isEqualTo(NO_CONTENT);
        return this;
    }

    public AbstractBodyAssertUtils<B> isHttpStatusIsBadRequest(){
        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
        return this;
    }

    public AbstractBodyAssertUtils<B> isHttpStatusIsConflict(){
        assertThat(response.getStatus()).isEqualTo(CONFLICT);
        return this;
    }

    public AbstractBodyAssertUtils<B> isHttpStatusIsInternalServerError(){
        assertThat(response.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR);
        return this;
    }

}