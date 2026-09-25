package service.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import service.user.entity.Agente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AgenteRepository extends JpaRepository<Agente, UUID> {

    Optional<Agente> findByUsuarioId(UUID usuarioId);
    List<Agente> findByOficinaId(UUID oficinaId);
    List<Agente> findByOficinaIdIn(List<UUID> oficinaIds);
}
