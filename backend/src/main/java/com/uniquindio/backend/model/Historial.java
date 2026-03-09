package com.uniquindio.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "historial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Historial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private Solicitud solicitud;

    @Column(nullable = false)
    private Instant fechaHora;

    @Column(nullable = false, length = 100)
    private String accion;

    @Column(nullable = false, length = 50)
    private String usuarioResponsable;

    @Column(length = 1000)
    private String observaciones;

    @PrePersist
    protected void onCreate() {
        if (fechaHora == null) {
            fechaHora = Instant.now();
        }
    }
}
