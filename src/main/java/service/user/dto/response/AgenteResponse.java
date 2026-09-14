package service.user.dto.response;


import java.util.UUID;

public record AgenteResponse(
        UUID id,
        UUID usuarioId,
        String nombreUsuario,
        UUID oficinaId,
        String nombreOficina,
        String zonaEspecialidad
) {}