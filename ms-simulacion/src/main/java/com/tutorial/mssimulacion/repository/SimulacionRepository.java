package com.tutorial.mssimulacion.repository;

import com.tutorial.mssimulacion.entity.SimulacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulacionRepository extends JpaRepository<SimulacionEntity, Integer> {
    // Métodos adicionales de consulta si los necesitas
}

