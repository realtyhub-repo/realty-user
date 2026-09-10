package service.user.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "agente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "usuario_id", unique = true, nullable = false)
    private UUID usuarioId;

    @Column(name = "oficina_id", nullable = false)
    private UUID oficinaId;

    @Column(name = "zona_especialidad", nullable = true)
    private String zonaEspecialidad;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
