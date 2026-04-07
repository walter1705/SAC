package com.uniquindio.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

import com.uniquindio.backend.model.enums.*;

@Entity
@Table(name = "solicitudes", indexes = {
    @Index(name = "idx_solicitud_estado", columnList = "estado"),
    @Index(name = "idx_solicitud_tipo", columnList = "tipo"),
    @Index(name = "idx_solicitud_prioridad", columnList = "prioridad")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String estudianteNombre;

    @Column(nullable = false, length = 100)
    private String estudianteCorreo;

    @Column(nullable = false, length = 20)
    private String estudianteTelefono;

    @Column(nullable = false, length = 50)
    private String estudianteIdentificacion;

    @Column(nullable = false, length = 200)
    private String asunto;

    @Column(nullable = false, length = 2000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CanalOrigen canalOrigen;

    @Column(nullable = false)
    private Instant fechaHoraRegistro;

    @Enumerated(EnumType.STRING)
    private TipoSolicitud tipo;

    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;

    @Column(length = 500)
    private String notaClasificacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado;

    @Column(length = 2000)
    private String resolucion;

    @Column(length = 1000)
    private String notasCierre;

    @PrePersist
    protected void onCreate() {
        if (fechaHoraRegistro == null) {
            fechaHoraRegistro = Instant.now();
        }
    }
}
