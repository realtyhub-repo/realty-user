package service.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import service.user.entity.RolUsuario;

public record ActualizarRolRequest(
       @NotNull
        RolUsuario rolUsuario
) {
}
