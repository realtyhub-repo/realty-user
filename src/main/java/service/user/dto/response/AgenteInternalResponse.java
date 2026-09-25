package service.user.dto.response;

import lombok.Builder;
import service.user.entity.Agente;
import service.user.entity.Usuario;

import java.util.UUID;

@Builder
public record AgenteInternalResponse(
        UUID id,
        UUID usuarioId,
        UUID oficinaId,
        String nombre

) {

    public static AgenteInternalResponse from(Agente agente, Usuario usuario){
        return AgenteInternalResponse.builder()
                .id(agente.getId())
                .usuarioId(agente.getUsuarioId())
                .oficinaId(agente.getOficinaId())
                .nombre(usuario.getNombre())
                .build();
    }
}
