package service.user.dto.request;

public record CrearUsuarioRequest(
        String email,
        String nombre,
        String urlFoto
) {}