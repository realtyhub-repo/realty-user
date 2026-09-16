package service.user.dto.request;

import jakarta.validation.constraints.NotNull;

public record ActualizarStatusRequest(

        @NotNull
        Boolean status

) {
}
