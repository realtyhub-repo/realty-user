package service.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import service.user.dto.request.ActualizarAgenteRequest;
import service.user.dto.request.CrearAgenteRequest;
import service.user.dto.response.AgenteResponse;
import service.user.dto.response.UsuarioDetalleResponse;
import service.user.entity.Agente;
import service.user.entity.Oficina;
import service.user.entity.RolUsuario;
import service.user.entity.Usuario;
import service.user.exception.*;
import service.user.repository.AgenteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgenteService {

    private final AgenteRepository agenteRepository;
    private final UsuarioService usuarioService;
    private final OficinaService oficinaService;

    public AgenteResponse crear(CrearAgenteRequest request, RolUsuario rolSolicitante) {

        if (rolSolicitante != RolUsuario.ADMINISTRADOR_CENTRAL &&
                rolSolicitante != RolUsuario.GERENTE_OFICINA
        ) {
            throw new AccesoNoAutorizadoException("Acceso no autorizado");
        }

        Usuario usuarioPorId = usuarioService.buscarUsuarioIdInterno(request.usuarioId());

        if (usuarioPorId.getRol() != RolUsuario.AGENTE &&
                usuarioPorId.getRol() != RolUsuario.GERENTE_OFICINA
        ) {
            throw new RolInvalidoParaAgenteException("No cuenta con el rol para esta función");
        }

        if (agenteRepository.findByUsuarioId(request.usuarioId()).isPresent()) {
            throw new AgenteYaExisteException("Este usuario ya está registrado como agente");
        }

        Oficina oficina = oficinaService.buscarOficinaIdInterno(request.oficinaId());

        Agente agenteBuild = Agente.builder()
                .usuarioId(request.usuarioId())
                .oficinaId(request.oficinaId())
                .zonaEspecialidad(request.zonaEspecialidad())
                .build();

        Agente agenteGuardado = agenteRepository.save(agenteBuild);

        return new AgenteResponse(
                agenteGuardado.getId(),
                agenteGuardado.getUsuarioId(),
                usuarioPorId.getNombre(),
                agenteGuardado.getOficinaId(),
                oficina.getNombre(),
                agenteGuardado.getZonaEspecialidad()
        );

    }

    public AgenteResponse obtenerPorId(UUID id){

        Agente agentePorId   = buscarAgenteIdInterno(id);
        Usuario usuarioPorId = usuarioService.buscarUsuarioIdInterno(agentePorId.getUsuarioId());
        Oficina oficinaPorId = oficinaService.buscarOficinaIdInterno(agentePorId.getOficinaId());

        return new AgenteResponse(
                agentePorId.getId(),
                usuarioPorId.getId(),
                usuarioPorId.getNombre(),
                oficinaPorId.getId(),
                oficinaPorId.getNombre(),
                agentePorId.getZonaEspecialidad()
                );
    }

    public void actualizar (UUID id, ActualizarAgenteRequest request, RolUsuario rolSolicitante){

        if (rolSolicitante != RolUsuario.ADMINISTRADOR_CENTRAL &&
                rolSolicitante != RolUsuario.GERENTE_OFICINA
        ) {
            throw new AccesoNoAutorizadoException("Acceso no autorizado");
        }

        Agente agentePorId = agenteRepository.findById(id).orElseThrow(()->
                new AgenteNoEncontradoException("Agente no encontrado")
                );

        if(oficinaService.validarExistencia(request.oficinaId())){
            throw new OficinaNoEncontradaException("Oficina no encontrada");
        }

        agentePorId.setOficinaId(request.oficinaId());

        if(request.zonaEspecialidad()!=null)
            agentePorId.setZonaEspecialidad(request.zonaEspecialidad());

        agenteRepository.save(agentePorId);
    }

    public List<AgenteResponse> listarPorOficina(UUID oficinaId){

        Oficina oficina = oficinaService.buscarOficinaIdInterno(oficinaId);
        
        List<Agente> agentesPorOficina = agenteRepository.findByOficinaId(oficinaId);

        List<UUID> usuariosId = agentesPorOficina.stream()
                .map(Agente::getUsuarioId)
                .toList();

        List<Usuario> usuariosPorId = usuarioService.obtenerUsuariosId(usuariosId);

        Map<UUID,Usuario> usuarioMap = usuariosPorId.stream()
                .collect(Collectors.toMap(
                        Usuario::getId,
                        usuario -> usuario
                ));

        List<AgenteResponse> agenteResponseList = new ArrayList<>();

        for(Agente a:agentesPorOficina){

            Usuario usuario = usuarioMap.get(a.getUsuarioId());

            agenteResponseList.add(new AgenteResponse(a.getId(),
                    usuario.getId(),
                    usuario.getNombre(),
                    oficina.getId(),
                    oficina.getNombre(),
                    a.getZonaEspecialidad()
            ));

        }

        return agenteResponseList;
    }


    private Agente buscarAgenteIdInterno(UUID agenteId) {

        return agenteRepository.findById(agenteId).orElseThrow(() ->
                new AgenteNoEncontradoException("Agente no encontrado")
        );

    }



}
