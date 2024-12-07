package com.tutorial.mssolicitud.repository;

import com.tutorial.mssolicitud.entity.SolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<SolicitudEntity, Integer> {
    List<SolicitudEntity> findByIdUsuario(int idUsuario);
}
