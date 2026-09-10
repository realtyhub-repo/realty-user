package service.user.dto.response;

import lombok.*;
import service.user.entity.RolUsuario;
import service.user.entity.Usuario;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UsuarioDetalleResponse {


    UUID id;
    String email;
    String nombre;

    String urlFoto;
    String telefono;
    RolUsuario rol;
    Boolean activo;
    LocalDateTime createdAt;

    public static UsuarioDetalleResponse from(Usuario usuario){
        return UsuarioDetalleResponse.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .urlFoto(usuario.getUrlFoto())
                .telefono(usuario.getTelefono())
                .rol(usuario.getRol())
                .activo(usuario.getActivo())
                .createdAt(usuario.getCreatedAt())
                .build();
    }


}
