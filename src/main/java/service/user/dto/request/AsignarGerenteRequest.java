package service.user.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AsignarGerenteRequest(
        @NotNull
        UUID gerenteId

){
}
