package service.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.user.dto.request.CrearUsuarioRequest;
import service.user.dto.response.UsuarioResponse;
import service.user.service.UsuarioService;

import java.util.UUID;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/internal")
    public ResponseEntity<UsuarioResponse> crearUsuario(@RequestBody CrearUsuarioRequest request){

        UsuarioResponse usuarioResponse = usuarioService.crearUsuario(request);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioResponse);

    }

    @GetMapping("/internal/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioId(@PathVariable UUID id){

        return ResponseEntity.ok(usuarioService.buscarUsuarioId(id));
    }

}
