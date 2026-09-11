package service.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.user.dto.request.CrearUsuarioRequest;
import service.user.dto.response.UsuarioDetalleResponse;
import service.user.dto.response.UsuarioResponse;
import service.user.entity.Usuario;
import service.user.exception.UsuarioNoEncontradoException;
import service.user.repository.UsuarioRepository;
import service.user.security.ContextoUsuario;
import service.user.security.UsuarioActual;
import service.user.service.UsuarioService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/internal")
    public ResponseEntity<UsuarioResponse> crearUsuario(@RequestBody CrearUsuarioRequest request){

        UsuarioResponse usuarioResponse = usuarioService.crearUsuario(request);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioResponse);

    }

    @GetMapping("/internal/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioId(@PathVariable UUID id){

        return ResponseEntity.ok(usuarioService.buscarUsuarioId(id));
    }

    @PostMapping("/ADMIN")
    public ResponseEntity<UsuarioDetalleResponse> cambiarRol(@UsuarioActual ContextoUsuario contexto){

        Usuario usuario = usuarioRepository.findById(contexto.userId()).orElseThrow(
                ()-> new UsuarioNoEncontradoException("no se encontro al usuario")
        );

        usuario.setRol(contexto.rol());
        Usuario guardado= usuarioRepository.save(usuario);
        return ResponseEntity.ok().body(UsuarioDetalleResponse.from(guardado));
    }

}
