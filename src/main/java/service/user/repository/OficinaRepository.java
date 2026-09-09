package service.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import service.user.entity.Oficina;

import java.util.List;
import java.util.UUID;

public interface OficinaRepository extends JpaRepository<Oficina, UUID> {
    List<Oficina> findByOficinaPadreId(UUID oficinaPadreId);
}
