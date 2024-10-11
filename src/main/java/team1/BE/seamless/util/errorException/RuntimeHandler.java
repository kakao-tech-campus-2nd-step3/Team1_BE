package team1.BE.seamless.util.errorException;

import org.springframework.http.HttpStatus;

public class RuntimeHandler extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public RuntimeHandler(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
