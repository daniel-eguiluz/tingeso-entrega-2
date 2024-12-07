package com.tutorial.msusuario.repository;

import com.tutorial.msusuario.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
    // Métodos de búsqueda personalizados si se requieren
}


