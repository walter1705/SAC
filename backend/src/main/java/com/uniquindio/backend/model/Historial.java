package com.uniquindio.backend.model;

import com.uniquindio.backend.model.enums.AccionHistorial;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AccionHistorial accion;

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
