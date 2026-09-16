package service.user.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.user.dto.request.ActualizarPerfilRequest;
import service.user.dto.request.ActualizarRolRequest;
import service.user.dto.request.ActualizarStatusRequest;
import service.user.dto.response.UsuarioDetalleResponse;
import service.user.entity.RolUsuario;
import service.user.security.ContextoUsuario;
import service.user.security.UsuarioActual;
import service.user.service.UsuarioService;

import java.util.List;
import java.util.UUID;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<UsuarioDetalleResponse> usuarioActual(@UsuarioActual ContextoUsuario usuario) {

        UsuarioDetalleResponse response = usuarioService.buscarUsuarioDetalle(usuario.userId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<Void> actualizarUsuario(@UsuarioActual ContextoUsuario usuario,
                                                  @Valid @RequestBody ActualizarPerfilRequest request) {

        usuarioService.actualizarUsuario(usuario.userId(), request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping()
    public ResponseEntity<List<UsuarioDetalleResponse>> listarUsuarios(
            @UsuarioActual ContextoUsuario usuario,
            @RequestParam(name = "activo", required = false) Boolean activo,
            @RequestParam(name = "rol", required = false) RolUsuario rol
    ) {

        List<UsuarioDetalleResponse> usuarioDetalleResponseList = usuarioService
                .listar(usuario.rol(), activo, rol);

        return ResponseEntity.ok(usuarioDetalleResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDetalleResponse> buscarUsuarioId(
            @UsuarioActual ContextoUsuario usuario,
            @PathVariable UUID id
    ) {

        UsuarioDetalleResponse response = usuarioService.buscarUsuarioDetalle(usuario.rol(), id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<Void> actualizarRol(
            @PathVariable UUID id,
            @Valid @RequestBody ActualizarRolRequest request,
            @UsuarioActual ContextoUsuario usuario
    ) {

        usuarioService.cambiarRol(
                id,
                request.rolUsuario(),
                usuario.rol(),
                usuario.userId()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> actualizarStatus(
            @PathVariable UUID id,
            @Valid @RequestBody ActualizarStatusRequest request,
            @UsuarioActual ContextoUsuario usuario
    ) {

        usuarioService.cambiarEstado(
                id,
                request.status(),
                usuario.rol(),
                usuario.userId()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
