package service.user.dto.request;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;

public record ActualizarPerfilRequest(
        @Nullable
        @Pattern(regexp = ".*\\S.*")
        String nombre,

        @Nullable
        String urlFoto,
        @Nullable
        String telefono

) {
}
