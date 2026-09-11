package service.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import service.user.dto.internal.ErrorResponse;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailYaRegistradoException.class)
    public ResponseEntity<ErrorResponse> handleEmailYaRegistrado(EmailYaRegistradoException ex){
        return construirRespuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AccesoNoAutorizadoException.class)
    public ResponseEntity<ErrorResponse> handleAccesoNoAutorizadoException(AccesoNoAutorizadoException ex){
        return construirRespuesta(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(AutoModificacionException.class)
    public ResponseEntity<ErrorResponse> handleAutoModificacionRolException(AutoModificacionException ex){
        return construirRespuesta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(RequestInconsistenteException.class)
    public ResponseEntity<ErrorResponse> handleRequestInconsistenteException(RequestInconsistenteException ex){
        return construirRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage() );
    }

    @ExceptionHandler(OficinaCentralRequeridaException.class)
    public ResponseEntity<ErrorResponse> handleOficinaCentralRequeridaException(OficinaCentralRequeridaException ex){
        return construirRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(OficinaNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleOficinaNoEncontradaException(OficinaNoEncontradaException ex){
        return construirRespuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(TipoOficinaInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleTipoOficinaInvalidoException(TipoOficinaInvalidoException ex){
        return construirRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> construirRespuesta(HttpStatus status, String mensaje){
        ErrorResponse error = new ErrorResponse(mensaje, status.value(), LocalDateTime.now());
        return ResponseEntity.status(status).body(error);

    }
}
