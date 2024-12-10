package com.tutorial.mscostos.repository;

import com.tutorial.mscostos.entity.CalculoCostosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalculoCostosRepository extends JpaRepository<CalculoCostosEntity, Integer> {
    List<CalculoCostosEntity> findByIdUsuario(Long idUsuario);
}
