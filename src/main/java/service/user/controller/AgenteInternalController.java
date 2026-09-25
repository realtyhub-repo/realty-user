package service.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.user.dto.response.AgenteInternalResponse;
import service.user.service.AgenteService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/agentes")
@RequiredArgsConstructor
public class AgenteInternalController {

    private final AgenteService agenteService;


    @GetMapping()
    public ResponseEntity<List<AgenteInternalResponse>> listarAgentesInterno(
            @RequestParam List<UUID> oficinaIds) {

        return ResponseEntity.ok(agenteService.listarAgentesInterno(oficinaIds));
    }
}
