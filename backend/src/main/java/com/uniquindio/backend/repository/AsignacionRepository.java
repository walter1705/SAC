package com.uniquindio.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uniquindio.backend.model.Asignacion;
import com.uniquindio.backend.model.Solicitud;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {

    List<Asignacion> findBySolicitudIdAndActivaTrue(Long solicitudId);

    Optional<Asignacion> findBySolicitudAndActivaTrue(Solicitud solicitud);

    List<Asignacion> findByUsuarioIdAndActivaTrue(Long usuarioId);
}
