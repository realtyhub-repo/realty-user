package service.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ActualizarOficinaRequest (

        @NotBlank
        String nombre,

        @NotBlank
        String region

){}
