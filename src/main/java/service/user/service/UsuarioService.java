package service.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import service.user.dto.CrearUsuarioRequest;
import service.user.entity.RolUsuario;
import service.user.dto.UsuarioResponse;
import service.user.entity.Usuario;
import service.user.exception.EmailYaRegistradoException;
import service.user.exception.UsuarioNoEncontradoException;
import service.user.repository.UsuarioRepository;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;


    public UsuarioResponse crearUsuario(CrearUsuarioRequest request){
        Usuario usuario = Usuario.builder()
                .email(request.email())
                .nombre(request.nombre())
                .urlFoto(request.urlFoto())
                .rol(RolUsuario.CLIENTE)
                .build();

        try {

            Usuario guardado = usuarioRepository.saveAndFlush(usuario);
            return new UsuarioResponse(
                    guardado.getId(),
                    guardado.getRol());

        }catch (DataIntegrityViolationException e){
            throw new EmailYaRegistradoException("Error de uso");
        }



    }


    public UsuarioResponse buscarUsuarioId(UUID usuarioId){


        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(()->
                        new UsuarioNoEncontradoException("Usuario no encontrado"));


        return new UsuarioResponse(
                usuario.getId(), usuario.getRol()
        );
    }git
}
