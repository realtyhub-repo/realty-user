package service.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AutoModificacionRolException extends RuntimeException {
    public AutoModificacionRolException(String message) {
        super(message);
    }
}
