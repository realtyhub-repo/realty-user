package service.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RolInvalidoException extends RuntimeException {
    public RolInvalidoException(String message) {
        super(message);
    }
}
