package service.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import service.user.dto.request.CrearUsuarioRequest;
import service.user.dto.response.UsuarioResponse;
import service.user.entity.RolUsuario;
import service.user.entity.Usuario;
import service.user.exception.AccesoNoAutorizadoException;
import service.user.exception.AutoModificacionException;
import service.user.exception.EmailYaRegistradoException;
import service.user.exception.UsuarioNoEncontradoException;
import service.user.repository.UsuarioRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void crearUsuario_deberiaLanzarExcepcionSiElEmailYaExiste() {

        CrearUsuarioRequest request =
                new CrearUsuarioRequest("existente@correo.com", "Karime", null);

        when(usuarioRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(new Usuario()));

        assertThrows(
                EmailYaRegistradoException.class,
                () -> usuarioService.crearUsuario(request)
        );

        verify(usuarioRepository, never()).saveAndFlush(any());
    }

    @Test
    void crearUsuario_deberiaCrearloConRolClientePorDefecto() {

        CrearUsuarioRequest request =
                new CrearUsuarioRequest("nuevo@correo.com", "Karime", null);

        when(usuarioRepository.findByEmail(request.email()))
                .thenReturn(Optional.empty());

        UUID idGenerado = UUID.randomUUID();

        Usuario usuarioGuardado = Usuario.builder()
                .id(idGenerado)
                .email(request.email())
                .rol(RolUsuario.CLIENTE)
                .build();

        when(usuarioRepository.saveAndFlush(any(Usuario.class)))
                .thenReturn(usuarioGuardado);

        UsuarioResponse respuesta = usuarioService.crearUsuario(request);

        assertEquals(idGenerado, respuesta.id());
        assertEquals(RolUsuario.CLIENTE, respuesta.rolUsuario());
    }

    @Test
    void crearUsuario_deberiaLanzarExcepcionSiLaBaseDeDatosDetectaDuplicadoSimultaneo() {

        CrearUsuarioRequest request =
                new CrearUsuarioRequest("carrera@correo.com", "Karime", null);

        when(usuarioRepository.findByEmail(request.email()))
                .thenReturn(Optional.empty());

        when(usuarioRepository.saveAndFlush(any(Usuario.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate key"));

        assertThrows(
                EmailYaRegistradoException.class,
                () -> usuarioService.crearUsuario(request)
        );
    }

    @Test
    void buscarUsuarioId_deberiaLanzarExcepcionSiNoExiste() {

        UUID id = UUID.randomUUID();

        when(usuarioRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                UsuarioNoEncontradoException.class,
                () -> usuarioService.buscarUsuarioId(id)
        );
    }

    @Test
    void cambiarRol_deberiaLanzarExcepcionSiElSolicitanteNoEsAdmin() {

        UUID id = UUID.randomUUID();
        UUID idSolicitante = UUID.randomUUID();

        assertThrows(
                AccesoNoAutorizadoException.class,
                () -> usuarioService.cambiarRol(
                        id,
                        RolUsuario.AGENTE,
                        RolUsuario.AGENTE,
                        idSolicitante
                )
        );

        verify(usuarioRepository, never()).findById(any());
    }

    @Test
    void cambiarRol_deberiaLanzarExcepcionSiIntentaCambiarSuPropioRol() {

        UUID miPropioId = UUID.randomUUID();

        assertThrows(
                AutoModificacionException.class,
                () -> usuarioService.cambiarRol(
                        miPropioId,
                        RolUsuario.CLIENTE,
                        RolUsuario.ADMINISTRADOR_CENTRAL,
                        miPropioId
                )
        );
    }

    @Test
    void cambiarRol_deberiaFuncionarSiEsAdminYNoEsElMismoUsuario() {

        UUID idObjetivo = UUID.randomUUID();
        UUID idAdmin = UUID.randomUUID();

        Usuario usuario = Usuario.builder()
                .id(idObjetivo)
                .rol(RolUsuario.CLIENTE)
                .build();

        when(usuarioRepository.findById(idObjetivo))
                .thenReturn(Optional.of(usuario));

        usuarioService.cambiarRol(
                idObjetivo,
                RolUsuario.AGENTE,
                RolUsuario.ADMINISTRADOR_CENTRAL,
                idAdmin
        );

        assertEquals(RolUsuario.AGENTE, usuario.getRol());

        verify(usuarioRepository).save(usuario);
    }

    @Test
    void cambiarEstado_deberiaLanzarExcepcionSiIntentaDesactivarseASiMismo() {

        UUID miPropioId = UUID.randomUUID();

        assertThrows(
                AutoModificacionException.class,
                () -> usuarioService.cambiarEstado(
                        miPropioId,
                        false,
                        RolUsuario.ADMINISTRADOR_CENTRAL,
                        miPropioId
                )
        );
    }
}