package service.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import service.user.dto.request.ActualizarOficinaRequest;
import service.user.dto.request.CrearOficinaRequest;
import service.user.dto.response.OficinaResponse;
import service.user.entity.Oficina;
import service.user.entity.RolUsuario;
import service.user.entity.TipoOficina;
import service.user.exception.*;
import service.user.repository.OficinaRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OficinaService {

    private final OficinaRepository oficinaRepository;



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

    public List<OficinaResponse> lista(){
        return oficinaRepository.findAll().stream()
                .map(OficinaResponse::from)
                .toList();
    }


    public OficinaResponse actualizar(UUID id, ActualizarOficinaRequest request, RolUsuario rolSolicitante){

        if(rolSolicitante!=RolUsuario.ADMINISTRADOR_CENTRAL)
            throw new AccesoNoAutorizadoException("Acceso no autorizado");

        Oficina oficina = oficinaRepository.findById(id).orElseThrow(
                ()-> new OficinaNoEncontradaException("Oficina no encontrada")
        );

        oficina.setNombre(request.nombre());
        oficina.setRegion(request.region());

        Oficina oficinaGuardada = oficinaRepository.save(oficina);

        return OficinaResponse.from(oficinaGuardada);
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


}
