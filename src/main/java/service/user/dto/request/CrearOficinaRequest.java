package service.user.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import service.user.entity.TipoOficina;

import java.util.UUID;

public record CrearOficinaRequest (

        @NotBlank
        String nombre,

        @NotBlank
        String region,

        @NotBlank
        TipoOficina tipo,

        @Nullable
        UUID oficinaCentralId

) {
}
