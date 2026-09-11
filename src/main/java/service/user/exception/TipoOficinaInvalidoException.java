package service.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TipoOficinaInvalidoException extends RuntimeException {
    public TipoOficinaInvalidoException(String message) {
        super(message);
    }
}
