package service.user.dto.response;

import java.util.UUID;

public record AgentePerfilResponse(
        UUID id,
        UUID usuarioId,
        String nombreUsuario,
        String urlFoto,
        UUID oficinaId,
        String nombreOficina,
        String zonaEspecialidad
) {}
