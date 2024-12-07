package com.tutorial.mssolicitud.service;

import com.tutorial.mssolicitud.entity.SolicitudEntity;
import com.tutorial.mssolicitud.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudService {

    @Autowired
    SolicitudRepository solicitudRepository;

    public List<SolicitudEntity> getAll() {
        return solicitudRepository.findAll();
    }

    public SolicitudEntity getById(int id) {
        return solicitudRepository.findById(id).orElse(null);
    }

    public List<SolicitudEntity> getByUsuarioId(int idUsuario) {
        return solicitudRepository.findByIdUsuario(idUsuario);
    }

    public SolicitudEntity save(SolicitudEntity solicitud) {
        // Establecer un estado inicial, por ejemplo "En proceso"
        if(solicitud.getEstado() == null || solicitud.getEstado().isEmpty()) {
            solicitud.setEstado("En proceso");
        }
        return solicitudRepository.save(solicitud);
    }

    public SolicitudEntity update(SolicitudEntity solicitud) {
        // Se asume que la solicitud ya existe
        return solicitudRepository.save(solicitud);
    }

    public boolean deleteById(int id) {
        if(solicitudRepository.existsById(id)) {
            solicitudRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
