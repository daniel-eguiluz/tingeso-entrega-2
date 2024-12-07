package com.tutorial.mssolicitud.repository;

import com.tutorial.mssolicitud.entity.UsuarioComprobanteIngresosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioComprobanteIngresosRepository extends JpaRepository<UsuarioComprobanteIngresosEntity, Long> {
    Optional<UsuarioComprobanteIngresosEntity> findByIdUsuario(Long idUsuario);
}
