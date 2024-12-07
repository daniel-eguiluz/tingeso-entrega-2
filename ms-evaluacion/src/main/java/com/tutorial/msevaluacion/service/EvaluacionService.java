package com.tutorial.msevaluacion.service;

import com.tutorial.msevaluacion.entity.EvaluacionEntity;
import com.tutorial.msevaluacion.repository.EvaluacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EvaluacionService {

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

    @Autowired
    EvaluacionRepository evaluacionRepository;

    public List<EvaluacionEntity> getAll() {
        return evaluacionRepository.findAll();
    }

    public EvaluacionEntity getById(int id) {
        return evaluacionRepository.findById(id).orElse(null);
    }

    public List<EvaluacionEntity> getBySolicitudId(int idSolicitud) {
        return evaluacionRepository.findByIdSolicitud(idSolicitud);
    }

    public EvaluacionEntity save(int idSolicitud, String resultado, String observaciones) {
        EvaluacionEntity e = new EvaluacionEntity();
        e.setIdSolicitud(idSolicitud);
        e.setResultado(resultado);
        e.setObservaciones(observaciones);
        e.setFechaEvaluacion(new Date());
        return evaluacionRepository.save(e);
    }

    public EvaluacionEntity update(EvaluacionEntity evaluacion) {
        return evaluacionRepository.save(evaluacion);
    }

    public boolean deleteById(int id) {
        if(evaluacionRepository.existsById(id)) {
            evaluacionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Map<String,Object> evaluar(Long idSolicitud) throws Exception {
        // Obtener datos del prestamo
        Map prestamo = restTemplate.getForObject("http://ms-solicitud-credito/solicitud/"+idSolicitud, Map.class);
        if(prestamo == null) throw new Exception("Prestamo no encontrado");

        Long idUsuario = ... // obtener idUsuario llamando a un endpoint en ms-solicitud-credito que relate usuario-prestamo

        Map usuario = restTemplate.getForObject("http://ms-registro-usuario/usuario/"+idUsuario, Map.class);
        if(usuario == null) throw new Exception("Usuario no encontrado");

        // Aplicar reglas (R1-R7) llamando a ms-banco (que podría ser M4 internamente) o aquí directamente.
        // Aquí se simplifica: imaginemos que M4 hace toda la lógica.

        boolean aprobado = true; // según lógica
        // Actualizar estado de la solicitud en M3
        Map<String,Object> updateReq = new HashMap<>();
        updateReq.put("id", idSolicitud);
        updateReq.put("estado", aprobado ? "Aprobada":"Rechazada");
        restTemplate.put("http://ms-solicitud-credito/solicitud", updateReq);

        Map<String,Object> resp = new HashMap<>();
        resp.put("aprobado", aprobado);
        return resp;
    }
}
