package com.tutorial.mssolicitud.service;

import com.tutorial.mssolicitud.entity.SolicitudEntity;
import com.tutorial.mssolicitud.repository.SolicitudRepository;

import com.tutorial.mssolicitud.entity.PrestamoEntity;
import com.tutorial.mssolicitud.repository.PrestamoRepository;

import com.tutorial.mssolicitud.entity.UsuarioPrestamoEntity;
import com.tutorial.mssolicitud.repository.UsuarioPrestamoRepository;

import com.tutorial.mssolicitud.entity.ComprobanteIngresosEntity;
import com.tutorial.mssolicitud.repository.ComprobanteIngresosRepository;

import com.tutorial.mssolicitud.entity.UsuarioComprobanteIngresosEntity;
import com.tutorial.mssolicitud.repository.UsuarioComprobanteIngresosRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class SolicitudService {

    @Autowired
    PrestamoRepository prestamoRepository;
    @Autowired
    UsuarioPrestamoRepository usuarioPrestamoRepository;
    @Autowired
    ComprobanteIngresosRepository comprobanteIngresosRepository;
    @Autowired
    UsuarioComprobanteIngresosRepository usuarioComprobanteIngresosRepository;

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

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

    public PrestamoEntity solicitarCredito(Long idUsuario, PrestamoEntity prestamo, ComprobanteIngresosEntity comprobante) throws Exception {
        // Verificar usuario en ms-registro-usuario
        Map usuario = restTemplate.getForObject("http://ms-registro-usuario/usuario/"+idUsuario, Map.class);
        if(usuario == null) {
            throw new Exception("Usuario no encontrado");
        }

        prestamo.setEstado("En proceso");
        PrestamoEntity prestamoGuardado = prestamoRepository.save(prestamo);

        ComprobanteIngresosEntity ciGuardado = comprobanteIngresosRepository.save(comprobante);

        UsuarioPrestamoEntity up = new UsuarioPrestamoEntity();
        up.setIdUsuario(idUsuario);
        up.setIdPrestamo(prestamoGuardado.getId());
        usuarioPrestamoRepository.save(up);

        UsuarioComprobanteIngresosEntity uci = new UsuarioComprobanteIngresosEntity();
        uci.setIdUsuario(idUsuario);
        uci.setIdComprobanteIngresos(ciGuardado.getId());
        usuarioComprobanteIngresosRepository.save(uci);

        return prestamoGuardado;
    }

    public PrestamoEntity getPrestamoById(Long id) throws Exception {
        return prestamoRepository.findById(id).orElseThrow(() -> new Exception("Prestamo no encontrado"));
    }

    public List<PrestamoEntity> getPrestamosUsuario(Long idUsuario) {
        return usuarioPrestamoRepository.findByIdUsuario(idUsuario)
                .map(up -> {
                    try {
                        return List.of(prestamoRepository.findById(up.getIdPrestamo()).orElse(null));
                    } catch(Exception e) {
                        return List.<PrestamoEntity>of();
                    }
                }).orElse(List.of());
    }
}


