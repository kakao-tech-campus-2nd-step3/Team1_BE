package team1.be.seamless.util.errorException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public final ResponseEntity<StatusResponse> handleResponseStatusException(
            ResponseStatusException ex, WebRequest request) {
        StatusResponse errorResponse = new StatusResponse(ex.getStatusCode().value(),
                ex.getReason());
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<StatusResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        StatusResponse errorResponse = new StatusResponse(ex.getStatusCode().value(),
                ex.getBindingResult().getFieldError().getDefaultMessage());
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }

    @ExceptionHandler(RuntimeHandler.class)
    public ResponseEntity<StatusResponse> handleSignatureException(RuntimeHandler ex,
                                                                   WebRequest request) {
        StatusResponse errorResponse = new StatusResponse(ex.getStatus().value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

}
