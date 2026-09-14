package service.user.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CrearAgenteRequest(

        @NotNull
        UUID usuarioId,
        @NotNull
        UUID oficinaId,

        String zonaEspecialidad

) {
}
