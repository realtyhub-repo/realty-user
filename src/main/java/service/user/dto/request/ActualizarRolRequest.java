package service.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import service.user.entity.RolUsuario;

public record ActualizarRolRequest(
       @NotBlank
        RolUsuario rolUsuario
) {
}
