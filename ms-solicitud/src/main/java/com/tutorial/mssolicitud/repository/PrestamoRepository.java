package com.tutorial.mssolicitud.repository;

import com.tutorial.mssolicitud.entity.PrestamoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepository extends JpaRepository<PrestamoEntity, Long> {
}
