package com.tutorial.msseguimiento.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class SeguimientoService {

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

    public Map<String,Object> obtenerDatosPrestamoPorUsuario(Long idUsuario) {
        Map solicitud = restTemplate.getForObject("http://ms-solicitud/solicitud/usuario/" + idUsuario, Map.class);
        if (solicitud == null) return null;

        Map<String,Object> resp = new HashMap<>();
        resp.put("id", solicitud.get("id"));
        resp.put("tipo", solicitud.get("tipo"));              // ya no se usa "tipoPrestamo", es "tipo"
        resp.put("plazo", solicitud.get("plazo"));
        resp.put("tasaInteres", solicitud.get("tasaInteres")); // aquí usamos la misma clave que ms-solicitud
        resp.put("monto", solicitud.get("monto"));
        resp.put("estado", solicitud.get("estado"));
        resp.put("valorPropiedad", solicitud.get("valorPropiedad"));

        return resp;
    }
}