package service.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.user.dto.CrearUsuarioRequest;
import service.user.dto.UsuarioResponse;
import service.user.service.UsuarioService;

import java.util.UUID;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("")
    public UsuarioResponse crearUsuario(@RequestBody CrearUsuarioRequest request){

        return usuarioService.crearUsuario(request);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioId(@PathVariable UUID id){

        return ResponseEntity.ok(usuarioService.buscarUsuarioId(id));
    }

}
