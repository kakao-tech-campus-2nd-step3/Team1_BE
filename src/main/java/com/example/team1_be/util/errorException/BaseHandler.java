package com.example.team1_be.util.errorException;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class BaseHandler extends ResponseStatusException {

    public BaseHandler(HttpStatusCode status) {
        super(status);
    }

    public BaseHandler(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public BaseHandler(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public BaseHandler(HttpStatusCode status, String reason,
        Throwable cause) {
        super(status, reason, cause);
    }

    public BaseHandler(HttpStatusCode status, String reason,
        Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
