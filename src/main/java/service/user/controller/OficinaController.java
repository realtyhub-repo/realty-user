package service.user.controller;


import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.user.dto.request.ActualizarOficinaRequest;
import service.user.dto.request.AsignarGerenteRequest;
import service.user.dto.request.CrearOficinaRequest;
import service.user.dto.response.AgenteResponse;
import service.user.dto.response.OficinaResponse;
import service.user.security.ContextoUsuario;
import service.user.security.UsuarioActual;
import service.user.service.AgenteService;
import service.user.service.OficinaService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/oficinas")
@RequiredArgsConstructor
public class OficinaController {

    private final OficinaService oficinaService;
    private final AgenteService agenteService;

    @PostMapping()
    public ResponseEntity<OficinaResponse> crearOficina(@UsuarioActual ContextoUsuario usuario,@Valid @RequestBody CrearOficinaRequest request){

       OficinaResponse response = oficinaService.crear(request,usuario.rol());
       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping()
    public ResponseEntity<List<OficinaResponse>> listarOficinas(){

        return ResponseEntity.ok(oficinaService.listar());

    }

    @GetMapping("/{id}")
    public ResponseEntity<OficinaResponse> buscar(@PathVariable UUID id){
       OficinaResponse response  =  oficinaService.buscarPorId(id);

       return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OficinaResponse> actualizarOficina(
            @PathVariable UUID id,
            @RequestBody  ActualizarOficinaRequest oficinaRequest,
            @UsuarioActual ContextoUsuario usuario
    ){

       OficinaResponse response =  oficinaService.actualizar(id, oficinaRequest,usuario.rol());

     return  ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/sucursales")
    public ResponseEntity<List<OficinaResponse>> oficinasHijas(@PathVariable UUID id){

        List<OficinaResponse> lista = oficinaService.obtenerSucursalesDeCentral(id);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}/agentes")
    public ResponseEntity<List<AgenteResponse>> listarAgentePorOficina(@PathVariable UUID id){

        List<AgenteResponse> agentes = agenteService.listarPorOficina(id);

        return ResponseEntity.status(HttpStatus.OK).body(agentes);
    }

    @PutMapping("/{id}/gerente")
    public ResponseEntity<Void> asignarGerente(
            @PathVariable UUID id,
            @UsuarioActual ContextoUsuario contexto,
            @Valid @RequestBody AsignarGerenteRequest request) {

        oficinaService.asignarGerente(id, contexto.rol(), request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/gerente")
    public ResponseEntity<Void> removerGerente(
            @PathVariable UUID id,
            @UsuarioActual ContextoUsuario contexto) {

        oficinaService.removerGerente(id, contexto.rol());
        return ResponseEntity.noContent().build();
    }

}
