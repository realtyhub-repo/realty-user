package service.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import service.user.dto.request.ActualizarOficinaRequest;
import service.user.dto.request.AsignarGerenteRequest;
import service.user.dto.request.CrearOficinaRequest;
import service.user.dto.response.OficinaResponse;
import service.user.entity.Oficina;
import service.user.entity.RolUsuario;
import service.user.entity.TipoOficina;
import service.user.entity.Usuario;
import service.user.exception.*;
import service.user.repository.OficinaRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OficinaServiceTest {

    @Mock
    private OficinaRepository oficinaRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private OficinaService oficinaService;

    @Test
    void crear_deberiaLanzarExcepcionSiElSolicitanteNoEsAdmin() {

        CrearOficinaRequest request = new CrearOficinaRequest(
                "Oficina Norte",
                "Antioquia",
                null,
                null,
                TipoOficina.CENTRAL,
                null
        );

        assertThrows(
                AccesoNoAutorizadoException.class,
                () -> oficinaService.crear(
                        request,
                        RolUsuario.AGENTE
                )
        );

        verify(oficinaRepository, never()).save(any());
    }

    @Test
    void crear_deberiaLanzarExcepcionSiCentralTieneOficinaCentralAsociada() {

        CrearOficinaRequest request = new CrearOficinaRequest(
                "Oficina Norte",
                "Antioquia",
                null,
                null,
                TipoOficina.CENTRAL,
                UUID.randomUUID()
        );

        assertThrows(
                RequestInconsistenteException.class,
                () -> oficinaService.crear(
                        request,
                        RolUsuario.ADMINISTRADOR_CENTRAL
                )
        );
    }

    @Test
    void crear_deberiaLanzarExcepcionSiSucursalNoTraeOficinaCentralId() {

        CrearOficinaRequest request = new CrearOficinaRequest(
                "Sucursal Sur",
                "Antioquia",
                null,
                null,
                TipoOficina.SUCURSAL,
                null
        );

        assertThrows(
                OficinaCentralRequeridaException.class,
                () -> oficinaService.crear(
                        request,
                        RolUsuario.ADMINISTRADOR_CENTRAL
                )
        );
    }

    @Test
    void crear_deberiaLanzarExcepcionSiLaOficinaCentralReferenciadaNoEsDeTipoCentral() {

        UUID oficinaCentralId = UUID.randomUUID();

        CrearOficinaRequest request = new CrearOficinaRequest(
                "Sucursal Sur",
                "Antioquia",
                null,
                null,
                TipoOficina.SUCURSAL,
                oficinaCentralId
        );

        Oficina oficinaQueNoEsCentral = Oficina.builder()
                .id(oficinaCentralId)
                .tipo(TipoOficina.SUCURSAL)
                .build();

        when(oficinaRepository.findById(oficinaCentralId))
                .thenReturn(Optional.of(oficinaQueNoEsCentral));

        assertThrows(
                TipoOficinaInvalidoException.class,
                () -> oficinaService.crear(
                        request,
                        RolUsuario.ADMINISTRADOR_CENTRAL
                )
        );
    }

    @Test
    void crear_deberiaCrearCorrectamenteUnaOficinaCentral() {

        CrearOficinaRequest request = new CrearOficinaRequest(
                "Oficina Norte",
                "Antioquia",
                null,
                null,
                TipoOficina.CENTRAL,
                null
        );

        Oficina oficinaGuardada = Oficina.builder()
                .id(UUID.randomUUID())
                .nombre("Oficina Norte")
                .tipo(TipoOficina.CENTRAL)
                .build();

        when(oficinaRepository.save(any(Oficina.class)))
                .thenReturn(oficinaGuardada);

        OficinaResponse respuesta = oficinaService.crear(
                request,
                RolUsuario.ADMINISTRADOR_CENTRAL
        );

        assertNotNull(respuesta);

        verify(oficinaRepository)
                .save(any(Oficina.class));
    }

    @Test
    void buscarPorId_deberiaLanzarExcepcionSiNoExiste() {

        UUID id = UUID.randomUUID();

        when(oficinaRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                OficinaNoEncontradaException.class,
                () -> oficinaService.buscarPorId(id)
        );
    }

    @Test
    void asignarGerente_deberiaLanzarExcepcionSiElUsuarioNoTieneRolGerente() {

        UUID oficinaId = UUID.randomUUID();
        UUID gerenteId = UUID.randomUUID();

        AsignarGerenteRequest request =
                new AsignarGerenteRequest(gerenteId);

        Oficina oficina = Oficina.builder()
                .id(oficinaId)
                .build();

        Usuario usuarioNoGerente = Usuario.builder()
                .id(gerenteId)
                .rol(RolUsuario.AGENTE)
                .build();

        when(oficinaRepository.findById(oficinaId))
                .thenReturn(Optional.of(oficina));

        when(usuarioService.buscarUsuarioIdInterno(gerenteId))
                .thenReturn(usuarioNoGerente);

        assertThrows(
                RolInvalidoException.class,
                () -> oficinaService.asignarGerente(
                        oficinaId,
                        RolUsuario.ADMINISTRADOR_CENTRAL,
                        request
                )
        );
    }

    @Test
    void asignarGerente_deberiaLanzarExcepcionSiYaGerenciaOtraOficina() {

        UUID oficinaId = UUID.randomUUID();
        UUID gerenteId = UUID.randomUUID();

        AsignarGerenteRequest request =
                new AsignarGerenteRequest(gerenteId);

        Oficina oficina = Oficina.builder()
                .id(oficinaId)
                .build();

        Usuario usuarioGerente = Usuario.builder()
                .id(gerenteId)
                .rol(RolUsuario.GERENTE_OFICINA)
                .build();

        Oficina otraOficinaQueYaGerencia = Oficina.builder()
                .id(UUID.randomUUID())
                .gerenteId(gerenteId)
                .build();

        when(oficinaRepository.findById(oficinaId))
                .thenReturn(Optional.of(oficina));

        when(usuarioService.buscarUsuarioIdInterno(gerenteId))
                .thenReturn(usuarioGerente);

        when(oficinaRepository.findByGerenteId(gerenteId))
                .thenReturn(Optional.of(otraOficinaQueYaGerencia));

        assertThrows(
                GerenteYaAsignadoException.class,
                () -> oficinaService.asignarGerente(
                        oficinaId,
                        RolUsuario.ADMINISTRADOR_CENTRAL,
                        request
                )
        );
    }

    @Test
    void asignarGerente_deberiaAsignarCorrectamenteSiTodoEsValido() {

        UUID oficinaId = UUID.randomUUID();
        UUID gerenteId = UUID.randomUUID();

        AsignarGerenteRequest request =
                new AsignarGerenteRequest(gerenteId);

        Oficina oficina = Oficina.builder()
                .id(oficinaId)
                .build();

        Usuario usuarioGerente = Usuario.builder()
                .id(gerenteId)
                .rol(RolUsuario.GERENTE_OFICINA)
                .build();

        when(oficinaRepository.findById(oficinaId))
                .thenReturn(Optional.of(oficina));

        when(usuarioService.buscarUsuarioIdInterno(gerenteId))
                .thenReturn(usuarioGerente);

        when(oficinaRepository.findByGerenteId(gerenteId))
                .thenReturn(Optional.empty());

        oficinaService.asignarGerente(
                oficinaId,
                RolUsuario.ADMINISTRADOR_CENTRAL,
                request
        );

        assertEquals(
                gerenteId,
                oficina.getGerenteId()
        );

        verify(oficinaRepository)
                .save(oficina);
    }

    @Test
    void removerGerente_deberiaLanzarExcepcionSiLaOficinaNoTieneGerente() {

        UUID id = UUID.randomUUID();

        Oficina oficinaSinGerente = Oficina.builder()
                .id(id)
                .gerenteId(null)
                .build();

        when(oficinaRepository.findById(id))
                .thenReturn(Optional.of(oficinaSinGerente));

        assertThrows(
                OficinaSinGerenteException.class,
                () -> oficinaService.removerGerente(
                        id,
                        RolUsuario.ADMINISTRADOR_CENTRAL
                )
        );
    }
}