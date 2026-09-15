package service.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import service.user.dto.request.ActualizarPerfilRequest;
import service.user.dto.request.CrearUsuarioRequest;
import service.user.dto.response.UsuarioDetalleResponse;
import service.user.entity.RolUsuario;
import service.user.dto.response.UsuarioResponse;
import service.user.entity.Usuario;
import service.user.exception.*;
import service.user.repository.UsuarioRepository;
import service.user.repository.UsuarioSpecifications;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;


    public UsuarioResponse crearUsuario(CrearUsuarioRequest request){

        if(usuarioRepository.findByEmail(request.email()).isPresent()){
            throw new EmailYaRegistradoException("Este correo ya está registrado");
        }

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
            throw new EmailYaRegistradoException("Este correo ya está registrado");
        }

    }


    public UsuarioResponse buscarUsuarioId(UUID usuarioId){


        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(()->
                        new UsuarioNoEncontradoException("Usuario no encontrado"));


        return new UsuarioResponse(
                usuario.getId(), usuario.getRol()
        );
    }

    public List<UsuarioDetalleResponse> listar(RolUsuario filtroRol, Boolean filtroActivo, RolUsuario rolSolicitante){

        if(rolSolicitante!=RolUsuario.ADMINISTRADOR_CENTRAL){
            throw new AccesoNoAutorizadoException("Acceso no autorizado");
        }

        Specification<Usuario> specification = Specification
                .where(UsuarioSpecifications.tieneRol(filtroRol))
                .and(UsuarioSpecifications.tieneEstado(filtroActivo));


        return usuarioRepository.findAll(specification)
                .stream()
                .map(UsuarioDetalleResponse::from)
                .toList();
    }

    public void actualizarUsuario(UUID id, ActualizarPerfilRequest request){
        Usuario usuarioPorId = usuarioRepository.findById(id)
                .orElseThrow(()->
                        new UsuarioNoEncontradoException("Usuario no encontrado")
                );

        if(request.nombre()!=null)
            usuarioPorId.setNombre(request.nombre());

        if(request.telefono()!=null)
            usuarioPorId.setTelefono(request.telefono());

        if(request.urlFoto()!=null)
            usuarioPorId.setUrlFoto(request.urlFoto());


        usuarioRepository.save(usuarioPorId);
    }

    public void cambiarRol(UUID id, RolUsuario nuevoRol, RolUsuario rolSolicitante, UUID idSolicitante){

        if(rolSolicitante!=RolUsuario.ADMINISTRADOR_CENTRAL)
            throw new AccesoNoAutorizadoException("Acceso no autorizado");

        if(id.equals(idSolicitante))
            throw new AutoModificacionException("No puedes cambiar tu propio rol de administrador");

        Usuario usuarioPorId = usuarioRepository.findById(id)
                .orElseThrow(()->
                        new UsuarioNoEncontradoException("Usuario no encontrado")
                );

        usuarioPorId.setRol(nuevoRol);
        usuarioRepository.save(usuarioPorId);
    }

    public void cambiarEstado(UUID id, Boolean activo, RolUsuario rolSolicitante, UUID idSolicitante){
        if(rolSolicitante!=RolUsuario.ADMINISTRADOR_CENTRAL)
            throw new AccesoNoAutorizadoException("Acceso no autorizado");


        if(id.equals(idSolicitante))
            throw new AutoModificacionException("No puedes cambiar tu propio estado ");

        Usuario usuarioPorId = usuarioRepository.findById(id)
                .orElseThrow(()->
                        new UsuarioNoEncontradoException("Usuario no encontrado")
                );

        usuarioPorId.setActivo(activo);
        usuarioRepository.save(usuarioPorId);

    }

    public Usuario buscarUsuarioIdInterno(UUID uuid){
        return usuarioRepository.findById(uuid).orElseThrow(()->
                new UsuarioNoEncontradoException("Usuario no encontrado")
                );
    }


    public List<Usuario> obtenerUsuariosId(List<UUID> uuidList){
        return usuarioRepository.findAllById(uuidList);
    }

}


