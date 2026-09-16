package service.user.dto.request;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

public record ActualizarPerfilRequest(
        @NotBlank
        String nombre,

        @Nullable
        String urlFoto,
        @Nullable
        String telefono

) {
}
