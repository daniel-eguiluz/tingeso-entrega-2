package com.tutorial.msevaluacion.repository;

import com.tutorial.msevaluacion.entity.EvaluacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluacionRepository extends JpaRepository<EvaluacionEntity, Integer> {
    List<EvaluacionEntity> findByIdSolicitud(int idSolicitud);
}


