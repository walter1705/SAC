package com.uniquindio.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uniquindio.backend.model.Historial;

import java.util.List;

@Repository
public interface HistorialRepository extends JpaRepository<Historial, Long> {

    List<Historial> findBySolicitudIdOrderByFechaHoraAsc(Long solicitudId);
}
