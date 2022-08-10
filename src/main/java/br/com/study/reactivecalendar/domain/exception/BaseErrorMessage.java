package br.com.study.reactivecalendar.domain.exception;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.text.MessageFormat;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public class BaseErrorMessage {

    private static final String DEFAULT_RESOURCE = "messages";

    public static final BaseErrorMessage GENERIC_EXCEPTION = new BaseErrorMessage("generic");
    public static final BaseErrorMessage GENERIC_BAD_REQUEST = new BaseErrorMessage("generic.badRequest");
    public static final BaseErrorMessage GENERIC_NOT_FOUND_EXCEPTION = new BaseErrorMessage("generic.notFound");
    public static final BaseErrorMessage GENERIC_METHOD_NOT_ALLOWED_EXCEPTION = new BaseErrorMessage("generic.methodNotAllowed");
    public static final BaseErrorMessage GENERIC_MAX_RETRIES = new BaseErrorMessage("generic.MaxRetries");
    public static final BaseErrorMessage USER_NOT_FOUND_EXCEPTION = new BaseErrorMessage("user.NotFound");
    public static final BaseErrorMessage APPOINTMENT_NOT_FOUND_EXCEPTION = new BaseErrorMessage("appointment.NotFound");
    public static final BaseErrorMessage USER_NOT_FOUND_BY_EMAIL_EXCEPTION= new BaseErrorMessage("user.NotFoundByEmail");
    public static final BaseErrorMessage USER_ALREADY_HAS_APPOINTMENT_IN_INTERVAL = new BaseErrorMessage("user.hasAppointment");

    private final String key;
    private String[] params;

    public BaseErrorMessage params(final String... params){
        this.params = ArrayUtils.clone(params);
        return this;
    }

    public String getMessage(){
        var message = tryGetMessageFromBundle();
        if (ArrayUtils.isNotEmpty(params)){
            final var fmt = new MessageFormat(message);
            message = fmt.format(params);
        }
        return message;
    }

    protected String tryGetMessageFromBundle() {
        return getResource().getString(key);
    }

    public ResourceBundle getResource(){
        return ResourceBundle.getBundle(DEFAULT_RESOURCE);
    }

}
