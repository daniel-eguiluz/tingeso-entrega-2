package com.tutorial.mssolicitud.repository;

import com.tutorial.mssolicitud.entity.UsuarioPrestamoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioPrestamoRepository extends JpaRepository<UsuarioPrestamoEntity, Long> {
    Optional<UsuarioPrestamoEntity> findByIdUsuario(Long idUsuario);
}

