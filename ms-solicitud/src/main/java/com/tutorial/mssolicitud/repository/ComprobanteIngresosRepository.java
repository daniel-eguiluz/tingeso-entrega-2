package com.tutorial.mssolicitud.repository;

import com.tutorial.mssolicitud.entity.ComprobanteIngresosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComprobanteIngresosRepository extends JpaRepository<ComprobanteIngresosEntity, Long>{
}