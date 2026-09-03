package service.user.dto;

public record CrearUsuarioRequest(
        String email,
        String nombre,
        String urlFoto
) {}