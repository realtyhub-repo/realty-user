package service.user.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AgenteYaExisteException extends RuntimeException {
    public AgenteYaExisteException(String message) {
        super(message);
    }
}
