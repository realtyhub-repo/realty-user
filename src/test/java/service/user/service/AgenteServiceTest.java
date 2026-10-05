package service.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import service.user.dto.request.ActualizarAgenteRequest;
import service.user.dto.request.CrearAgenteRequest;
import service.user.dto.response.AgenteResponse;
import service.user.entity.Agente;
import service.user.entity.Oficina;
import service.user.entity.RolUsuario;
import service.user.entity.Usuario;
import service.user.exception.*;
import service.user.repository.AgenteRepository;
import service.user.repository.OficinaRepository;
import service.user.repository.UsuarioRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgenteServiceTest {

    @Mock
    private AgenteRepository agenteRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private OficinaService oficinaService;

    @Mock
    private OficinaRepository oficinaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AgenteService agenteService;

    @Test
    void crear_deberiaLanzarExcepcionSiElSolicitanteNoEsAdminNiGerente() {

        CrearAgenteRequest request = new CrearAgenteRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Norte"
        );

        assertThrows(
                AccesoNoAutorizadoException.class,
                () -> agenteService.crear(
                        request,
                        RolUsuario.CLIENTE,
                        UUID.randomUUID()
                )
        );

        verify(usuarioService, never())
                .buscarUsuarioIdInterno(any());
    }

    @Test
    void crear_deberiaLanzarExcepcionSiElGerenteNoTieneOficinaAsignada() {

        UUID idGerente = UUID.randomUUID();

        CrearAgenteRequest request = new CrearAgenteRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Norte"
        );

        when(oficinaRepository.findByGerenteId(idGerente))
                .thenReturn(Optional.empty());

        assertThrows(
                AccesoNoAutorizadoException.class,
                () -> agenteService.crear(
                        request,
                        RolUsuario.GERENTE_OFICINA,
                        idGerente
                )
        );
    }

    @Test
    void crear_deberiaLanzarExcepcionSiElGerenteIntentaCrearAgenteEnOtraOficina() {

        UUID idGerente = UUID.randomUUID();
        UUID oficinaDelGerenteId = UUID.randomUUID();
        UUID oficinaSolicitadaId = UUID.randomUUID();

        CrearAgenteRequest request = new CrearAgenteRequest(
                UUID.randomUUID(),
                oficinaSolicitadaId,
                "Norte"
        );

        Oficina oficinaDelGerente = Oficina.builder()
                .id(oficinaDelGerenteId)
                .build();

        when(oficinaRepository.findByGerenteId(idGerente))
                .thenReturn(Optional.of(oficinaDelGerente));

        assertThrows(
                AccesoNoAutorizadoException.class,
                () -> agenteService.crear(
                        request,
                        RolUsuario.GERENTE_OFICINA,
                        idGerente
                )
        );
    }

    @Test
    void crear_deberiaLanzarExcepcionSiElUsuarioNoTieneRolValido() {

        UUID usuarioId = UUID.randomUUID();

        CrearAgenteRequest request = new CrearAgenteRequest(
                usuarioId,
                UUID.randomUUID(),
                "Norte"
        );

        Usuario usuarioCliente = Usuario.builder()
                .id(usuarioId)
                .rol(RolUsuario.CLIENTE)
                .build();

        when(usuarioService.buscarUsuarioIdInterno(usuarioId))
                .thenReturn(usuarioCliente);

        assertThrows(
                RolInvalidoParaAgenteException.class,
                () -> agenteService.crear(
                        request,
                        RolUsuario.ADMINISTRADOR_CENTRAL,
                        UUID.randomUUID()
                )
        );
    }

    @Test
    void crear_deberiaLanzarExcepcionSiElUsuarioYaEsAgente() {

        UUID usuarioId = UUID.randomUUID();

        CrearAgenteRequest request = new CrearAgenteRequest(
                usuarioId,
                UUID.randomUUID(),
                "Norte"
        );

        Usuario usuarioAgente = Usuario.builder()
                .id(usuarioId)
                .rol(RolUsuario.AGENTE)
                .build();

        when(usuarioService.buscarUsuarioIdInterno(usuarioId))
                .thenReturn(usuarioAgente);

        when(agenteRepository.findByUsuarioId(usuarioId))
                .thenReturn(Optional.of(new Agente()));

        assertThrows(
                AgenteYaExisteException.class,
                () -> agenteService.crear(
                        request,
                        RolUsuario.ADMINISTRADOR_CENTRAL,
                        UUID.randomUUID()
                )
        );
    }

    @Test
    void crear_deberiaCrearloCorrectamenteSiTodoEsValido() {

        UUID usuarioId = UUID.randomUUID();
        UUID oficinaId = UUID.randomUUID();

        CrearAgenteRequest request = new CrearAgenteRequest(
                usuarioId,
                oficinaId,
                "Norte"
        );

        Usuario usuario = Usuario.builder()
                .id(usuarioId)
                .nombre("Karime")
                .rol(RolUsuario.AGENTE)
                .build();

        Oficina oficina = Oficina.builder()
                .id(oficinaId)
                .nombre("Oficina Centro")
                .build();

        Agente agenteGuardado = Agente.builder()
                .id(UUID.randomUUID())
                .usuarioId(usuarioId)
                .oficinaId(oficinaId)
                .zonaEspecialidad("Norte")
                .build();

        when(usuarioService.buscarUsuarioIdInterno(usuarioId))
                .thenReturn(usuario);

        when(agenteRepository.findByUsuarioId(usuarioId))
                .thenReturn(Optional.empty());

        when(oficinaService.buscarOficinaIdInterno(oficinaId))
                .thenReturn(oficina);

        when(agenteRepository.save(any(Agente.class)))
                .thenReturn(agenteGuardado);

        AgenteResponse respuesta = agenteService.crear(
                request,
                RolUsuario.ADMINISTRADOR_CENTRAL,
                UUID.randomUUID()
        );

        assertEquals(usuarioId, respuesta.usuarioId());
        assertEquals("Karime", respuesta.nombreUsuario());
        assertEquals("Oficina Centro", respuesta.nombreOficina());
    }

    @Test
    void obtenerPorId_deberiaLanzarExcepcionSiElAgenteNoExiste() {

        UUID usuarioId = UUID.randomUUID();

        when(agenteRepository.findByUsuarioId(usuarioId))
                .thenReturn(Optional.empty());

        assertThrows(
                AgenteNoEncontradoException.class,
                () -> agenteService.obtenerPorId(usuarioId)
        );
    }

    @Test
    void actualizar_deberiaLanzarExcepcionSiElSolicitanteNoEsAdminNiGerente() {

        ActualizarAgenteRequest request = new ActualizarAgenteRequest(
                UUID.randomUUID(),
                "Sur"
        );

        assertThrows(
                AccesoNoAutorizadoException.class,
                () -> agenteService.actualizar(
                        UUID.randomUUID(),
                        request,
                        RolUsuario.CLIENTE,
                        UUID.randomUUID()
                )
        );

        verify(agenteRepository, never())
                .findById(any());
    }

    @Test
    void actualizar_deberiaLanzarExcepcionSiElAgenteNoExiste() {

        UUID id = UUID.randomUUID();

        ActualizarAgenteRequest request = new ActualizarAgenteRequest(
                UUID.randomUUID(),
                "Sur"
        );

        when(agenteRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                AgenteNoEncontradoException.class,
                () -> agenteService.actualizar(
                        id,
                        request,
                        RolUsuario.ADMINISTRADOR_CENTRAL,
                        UUID.randomUUID()
                )
        );
    }

    @Test
    void actualizar_deberiaLanzarExcepcionSiLaOficinaNoExiste() {

        UUID id = UUID.randomUUID();
        UUID oficinaId = UUID.randomUUID();

        ActualizarAgenteRequest request = new ActualizarAgenteRequest(
                oficinaId,
                "Sur"
        );

        Agente agente = Agente.builder()
                .id(id)
                .usuarioId(UUID.randomUUID())
                .oficinaId(UUID.randomUUID())
                .build();

        when(agenteRepository.findById(id))
                .thenReturn(Optional.of(agente));

        when(oficinaService.validarExistencia(oficinaId))
                .thenReturn(true);

        assertThrows(
                OficinaNoEncontradaException.class,
                () -> agenteService.actualizar(
                        id,
                        request,
                        RolUsuario.ADMINISTRADOR_CENTRAL,
                        UUID.randomUUID()
                )
        );
    }
}