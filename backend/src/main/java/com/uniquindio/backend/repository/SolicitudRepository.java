package com.uniquindio.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uniquindio.backend.model.Solicitud;
import com.uniquindio.backend.model.enums.EstadoSolicitud;
import com.uniquindio.backend.model.enums.Prioridad;
import com.uniquindio.backend.model.enums.TipoSolicitud;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long>, JpaSpecificationExecutor<Solicitud> {

    @Query("SELECT s FROM Solicitud s " +
           "LEFT JOIN Asignacion a ON a.solicitud = s AND a.activa = true " +
           "WHERE (:estado IS NULL OR s.estado = :estado) " +
           "AND (:tipo IS NULL OR s.tipo = :tipo) " +
           "AND (:prioridad IS NULL OR s.prioridad = :prioridad) " +
           "AND (:responsableId IS NULL OR a.usuario.id = :responsableId)")
    Page<Solicitud> findWithFilters(
            @Param("estado") EstadoSolicitud estado,
            @Param("tipo") TipoSolicitud tipo,
            @Param("prioridad") Prioridad prioridad,
            @Param("responsableId") Long responsableId,
            Pageable pageable
    );
}
