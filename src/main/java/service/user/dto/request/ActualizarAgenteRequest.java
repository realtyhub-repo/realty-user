package service.user.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ActualizarAgenteRequest(

        @NotNull
        UUID oficinaId,
        @Nullable
        String zonaEspecialidad

) {
}
