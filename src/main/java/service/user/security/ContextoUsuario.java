package service.user.security;

import service.user.entity.RolUsuario;
import java.util.UUID;

public record ContextoUsuario(UUID userId, RolUsuario rol) {}