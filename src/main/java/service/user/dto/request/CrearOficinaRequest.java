package service.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import service.user.entity.TipoOficina;

import java.util.UUID;

public record CrearOficinaRequest (

        @NotBlank
        String nombre,

        @NotBlank
        String region,

        @NotBlank
        TipoOficina tipo,

        UUID oficinaCentralId

) {
}
