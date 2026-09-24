package service.user.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import service.user.entity.TipoOficina;

import java.util.UUID;

public record CrearOficinaRequest (

        @NotNull
        String nombre,

        @NotBlank
        String region,

        @DecimalMin(value = "-90.0")
        @DecimalMax(value = "90.0")
        Double latitud,

        @DecimalMin(value = "-180.0")
        @DecimalMax(value = "180.0")
        Double longitud,

        @NotNull
        TipoOficina tipo,

        @Nullable
        UUID oficinaCentralId

) {
}
