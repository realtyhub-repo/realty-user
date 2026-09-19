package service.user.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record ActualizarOficinaRequest (

        @Nullable
        String nombre,

        @Nullable
        String region

){}
