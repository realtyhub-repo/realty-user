package service.user.dto.request;


import jakarta.validation.constraints.NotBlank;

public record ActualizarPerfilRequest(
        @NotBlank
        String nombre,

        String urlFoto,
        String telefono

) {
}
