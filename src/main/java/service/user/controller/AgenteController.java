package service.user.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.user.dto.request.ActualizarAgenteRequest;
import service.user.dto.request.CrearAgenteRequest;
import service.user.dto.response.AgenteResponse;
import service.user.security.ContextoUsuario;
import service.user.security.UsuarioActual;
import service.user.service.AgenteService;

import java.util.UUID;

@RestController
@RequestMapping("/agentes")
@RequiredArgsConstructor
public class AgenteController {

    private final AgenteService agenteService;

    @PostMapping()
    public ResponseEntity<AgenteResponse> crearAgente(@Valid @RequestBody CrearAgenteRequest request, @UsuarioActual ContextoUsuario usuario){

        AgenteResponse agenteResponse = agenteService.crear(request, usuario.rol(), usuario.userId());

        return ResponseEntity.ok(agenteResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgenteResponse> buscarAgentePorId(@PathVariable UUID id){
        AgenteResponse agenteResponse = agenteService.obtenerPorId(id);

        return ResponseEntity.ok(agenteResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarAgente(
            @PathVariable UUID id,
            @Valid @RequestBody ActualizarAgenteRequest request,
            @UsuarioActual ContextoUsuario usuario
                                                 ){

        agenteService.actualizar(id,request, usuario.rol(), usuario.userId());

        return ResponseEntity.noContent().build();
    }

}
