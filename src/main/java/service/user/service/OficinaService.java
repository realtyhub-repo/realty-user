package service.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OficinaService {

    private final OficinaRepository oficinaRepository;
    private final UsuarioService usuarioService;


    public OficinaResponse crear(CrearOficinaRequest request, RolUsuario rolSolicitante){

        if(rolSolicitante!=RolUsuario.ADMINISTRADOR_CENTRAL)
            throw new AccesoNoAutorizadoException("Acceso no autorizado");

        if(TipoOficina.CENTRAL==request.tipo() && request.oficinaCentralId()!=null)
            throw new RequestInconsistenteException("Una oficina de tipo CENTRAL no puede tener una oficina central asociada");


        if(TipoOficina.SUCURSAL==request.tipo() && request.oficinaCentralId()==null)
            throw new OficinaCentralRequeridaException("El id de la oficina central es requerido");


        if(request.tipo()==TipoOficina.SUCURSAL){
            Oficina oficinaPorId = oficinaRepository.findById(request.oficinaCentralId()).orElseThrow(()->
                    new OficinaNoEncontradaException("Oficina no encontrada")
                    );

            if(oficinaPorId.getTipo()!=TipoOficina.CENTRAL)
                throw new TipoOficinaInvalidoException("Tipo de oficina invalido");
        }



        Oficina oficinaCreada = Oficina.builder()
                .nombre(request.nombre())
                .region(request.region())
                .latitud(request.latitud())
                .longitud(request.longitud())
                .tipo(request.tipo())
                .oficinaCentralId(request.oficinaCentralId())
                .build();

        Oficina oficinaGuardada = oficinaRepository.save(oficinaCreada);

        return OficinaResponse.from(oficinaGuardada);
    }

    public OficinaResponse buscarPorId(UUID id){
        Oficina oficina = oficinaRepository.findById(id).orElseThrow(
                ()-> new OficinaNoEncontradaException("Oficina no encontrada")
        );

        return OficinaResponse.from(oficina);
    }

    public List<OficinaResponse> listar(){
        return oficinaRepository.findAll().stream()
                .map(OficinaResponse::from)
                .toList();
    }


    public OficinaResponse actualizar(UUID id, ActualizarOficinaRequest request, RolUsuario rolSolicitante){

        if(rolSolicitante!=RolUsuario.ADMINISTRADOR_CENTRAL)
            throw new AccesoNoAutorizadoException("Acceso no autorizado");

        if(request.nombre()==null && request.region()==null)
            throw new RequestInconsistenteException("Es requerido al menos un campo");

        Oficina oficina = oficinaRepository.findById(id).orElseThrow(
                ()-> new OficinaNoEncontradaException("Oficina no encontrada")
        );


        if(request.nombre()!=null)
            oficina.setNombre(request.nombre());

        if(request.region()!=null)
            oficina.setRegion(request.region());

        if(request.latitud()!=null)
            oficina.setLatitud(request.latitud());

        if(request.longitud()!=null)
            oficina.setLongitud(request.longitud());

        Oficina oficinaGuardada = oficinaRepository.save(oficina);

        return OficinaResponse.from(oficinaGuardada);
    }


    public void asignarGerente(UUID oficinaId, RolUsuario rolSolicitante, AsignarGerenteRequest request){
        if(rolSolicitante!=RolUsuario.ADMINISTRADOR_CENTRAL)
            throw new AccesoNoAutorizadoException("Acceso no autorizado");

        Oficina oficina = buscarOficinaIdInterno(oficinaId);

        Usuario usuario = usuarioService.buscarUsuarioIdInterno(request.gerenteId());

        if(usuario.getRol()!=RolUsuario.GERENTE_OFICINA)
            throw new RolInvalidoException("El usuario no tiene rol GERENTE_OFICINA");

        Optional<Oficina> oficinaOptional = oficinaRepository.findByGerenteId(request.gerenteId());



        if(oficinaOptional.isPresent())
            throw new GerenteYaAsignadoException("Este usuario ya gerencia otra oficina");


        oficina.setGerenteId(request.gerenteId());
        oficinaRepository.save(oficina);
    }

    public List<OficinaResponse> obtenerSucursalesDeCentral(UUID oficinaCentralId){
        Oficina oficinaCentral = oficinaRepository.findById(oficinaCentralId)
                .orElseThrow(()-> new OficinaNoEncontradaException("Oficina no encontrada"));

        if(oficinaCentral.getTipo()!=TipoOficina.CENTRAL)
            throw new TipoOficinaInvalidoException("Tipo de oficina invalido");


        return oficinaRepository.findByOficinaCentralId(oficinaCentralId).stream()
                .map(OficinaResponse::from)
                .toList();

    }

    public Oficina buscarOficinaIdInterno(UUID id){
        return  oficinaRepository.findById(id)
                .orElseThrow(() -> new OficinaNoEncontradaException("Oficina no encontrada"));
    }

    public Boolean validarExistencia(UUID id){
        return oficinaRepository.findById(id).isEmpty();
    }


    public void removerGerente(UUID id, RolUsuario rolSolicitante) {
        if(rolSolicitante!=RolUsuario.ADMINISTRADOR_CENTRAL)
            throw new AccesoNoAutorizadoException("Acceso no autorizado");

        Oficina oficina = buscarOficinaIdInterno(id);

        if (oficina.getGerenteId()==null)
            throw new OficinaSinGerenteException("Oficina aun sin gerente");

        oficina.setGerenteId(null);
        oficinaRepository.save(oficina);
    }
}
