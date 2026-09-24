package service.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import service.user.entity.Oficina;
import service.user.entity.TipoOficina;

import java.util.UUID;

@Builder
@Getter
public class OficinaResponse {


    private UUID id;
    private String nombre;
    private String region;
    private Double latitud;
    private Double longitud;
    private TipoOficina tipo;
    private UUID oficinaCentralId;
    private UUID gerenteId;

    public static OficinaResponse from(Oficina oficina){
        return  OficinaResponse.builder()
                .id(oficina.getId())
                .nombre(oficina.getNombre())
                .region(oficina.getRegion())
                .latitud(oficina.getLatitud())
                .longitud(oficina.getLongitud())
                .tipo(oficina.getTipo())
                .oficinaCentralId(oficina.getOficinaCentralId())
                .gerenteId(oficina.getGerenteId())
                .build();
    }
}
