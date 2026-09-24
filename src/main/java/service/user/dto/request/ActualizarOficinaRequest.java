package service.user.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record ActualizarOficinaRequest (

        @Nullable
        String nombre,

        @Nullable
        String region,

        @DecimalMin(value = "-90.0")
        @DecimalMax(value = "90.0")
        Double latitud,

        @DecimalMin(value = "-180.0")
        @DecimalMax(value = "180.0")
        Double longitud

){}
