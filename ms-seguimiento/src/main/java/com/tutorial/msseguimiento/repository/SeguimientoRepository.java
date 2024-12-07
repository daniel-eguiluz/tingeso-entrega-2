package com.tutorial.msseguimiento.repository;

import com.tutorial.msseguimiento.entity.SeguimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeguimientoRepository extends JpaRepository<SeguimientoEntity, Integer> {
    List<SeguimientoEntity> findByIdSolicitud(int idSolicitud);
}
