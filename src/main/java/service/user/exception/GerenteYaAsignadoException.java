package service.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class GerenteYaAsignadoException extends RuntimeException {
    public GerenteYaAsignadoException(String message) {
        super(message);
    }
}
