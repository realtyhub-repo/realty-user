package service.user.dto.response;

import lombok.Builder;
import service.user.entity.Usuario;

import java.util.UUID;

@Builder
public record UsuarioContactoInternalResponse(

        UUID id,
        String nombre,
        String telefono,
        String email

) {
    public static UsuarioContactoInternalResponse from(Usuario usuario){
        return UsuarioContactoInternalResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .telefono(usuario.getTelefono())
                .email(usuario.getEmail())
                .build();
    }
}
