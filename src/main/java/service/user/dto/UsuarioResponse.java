package service.user.dto;

import service.user.entity.RolUsuario;

import java.util.UUID;

public record UsuarioResponse (UUID id, RolUsuario rolUsuario){
}
