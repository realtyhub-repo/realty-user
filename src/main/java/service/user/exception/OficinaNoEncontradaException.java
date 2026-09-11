package service.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OficinaNoEncontradaException extends RuntimeException {
    public OficinaNoEncontradaException(String message) {
        super(message);
    }
}
