package com.tutorial.msseguimiento.service;

import com.tutorial.msseguimiento.entity.SeguimientoEntity;
import com.tutorial.msseguimiento.repository.SeguimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeguimientoService {

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

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

    public Map<String,Object> obtenerEstado(Long idSolicitud) {
        Map prestamo = restTemplate.getForObject("http://ms-solicitud-credito/solicitud/"+idSolicitud, Map.class);
        if(prestamo == null) return null;
        Map<String,Object> resp = new HashMap<>();
        resp.put("estado", prestamo.get("estado"));
        return resp;
    }
}
