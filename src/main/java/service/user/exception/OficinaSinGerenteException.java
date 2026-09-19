package service.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OficinaSinGerenteException extends RuntimeException {
    public OficinaSinGerenteException(String message) {
        super(message);
    }
}
