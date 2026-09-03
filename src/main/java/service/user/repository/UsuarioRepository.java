package service.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import service.user.entity.Usuario;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
}
