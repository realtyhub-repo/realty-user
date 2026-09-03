package service.user.dto;

import java.util.UUID;

public record UsuarioResponse (UUID id, RolUsuario rolUsuario){
}
