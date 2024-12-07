package com.tutorial.msseguimiento.service;

import com.tutorial.msseguimiento.entity.SeguimientoEntity;
import com.tutorial.msseguimiento.repository.SeguimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SeguimientoService {

    @Autowired
    SeguimientoRepository seguimientoRepository;

    public List<SeguimientoEntity> getAll() {
        return seguimientoRepository.findAll();
    }

    public SeguimientoEntity getById(int id) {
        return seguimientoRepository.findById(id).orElse(null);
    }

    public List<SeguimientoEntity> getBySolicitudId(int idSolicitud) {
        return seguimientoRepository.findByIdSolicitud(idSolicitud);
    }

    public SeguimientoEntity save(int idSolicitud, String estadoActual) {
        SeguimientoEntity seg = new SeguimientoEntity();
        seg.setIdSolicitud(idSolicitud);
        seg.setEstadoActual(estadoActual);
        seg.setFechaActualizacion(new Date());
        return seguimientoRepository.save(seg);
    }

    public SeguimientoEntity update(SeguimientoEntity seguimiento) {
        return seguimientoRepository.save(seguimiento);
    }

    public boolean deleteById(int id) {
        if(seguimientoRepository.existsById(id)) {
            seguimientoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
