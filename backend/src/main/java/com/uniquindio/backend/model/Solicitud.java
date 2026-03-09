package com.uniquindio.backend.entity;

import com.uniquindio.backend.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "solicitudes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSolicitud tipo;

    @Column(nullable = false, length = 2000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CanalOrigen canalOrigen;

    @Column(nullable = false)
    private Instant fechaHoraRegistro;

    @Column(nullable = false, length = 50)
    private String idSolicitante;

    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;

    @Column(length = 500)
    private String justificacionPrioridad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado;

    @Column(length = 2000)
    private String observacionCierre;

    @PrePersist
    protected void onCreate() {
        if (fechaHoraRegistro == null) {
            fechaHoraRegistro = Instant.now();
        }
        if (estado == null) {
            estado = EstadoSolicitud.REGISTRADA;
        }
    }
}
