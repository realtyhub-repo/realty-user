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
    private TipoOficina tipo;
    private UUID oficinaCentralId;

    public static OficinaResponse from(Oficina oficina){
        return  OficinaResponse.builder()
                .id(oficina.getId())
                .nombre(oficina.getNombre())
                .region(oficina.getRegion())
                .tipo(oficina.getTipo())
                .oficinaCentralId(oficina.getOficinaCentralId())
                .build();
    }
}
