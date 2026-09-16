package service.user.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "oficina")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Oficina {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoOficina tipo;

    @Column(name = "oficina_central_id", nullable = true)
    private UUID oficinaCentralId;

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
