package com.tutorial.mssimulacion.service;

import com.tutorial.mssimulacion.entity.SimulacionEntity;
import com.tutorial.mssimulacion.repository.SimulacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SimulacionService {

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

    public Map<String,Object> simularCredito(Long idUsuario) throws Exception {
        // Llamar a ms-solicitud para obtener datos del préstamo asociado a ese usuario
        // Este endpoint debe devolver al menos: "monto", "plazo", "tasaInteres"
        // Asegúrate que ms-solicitud en /solicitud/usuario/{idUsuario} devuelva también "idPrestamo" si lo necesitas
        Map prestamo = restTemplate.getForObject("http://ms-solicitud/solicitud/usuario/" + idUsuario, Map.class);

        if (prestamo == null) {
            throw new Exception("No se encontraron datos del préstamo para el usuario " + idUsuario);
        }

        double tasaInteresAnual = ((Number) prestamo.get("tasaInteres")).doubleValue();
        int plazoEnAnios = ((Number) prestamo.get("plazo")).intValue();
        int monto = ((Number) prestamo.get("monto")).intValue();

        // Cálculo de la tasa mensual
        double tasaInteresMensual = (tasaInteresAnual / 12) / 100;
        int numeroPagos = plazoEnAnios * 12;

        // Cálculo del pago mensual usando fórmula de amortización
        double pagoMensual = (monto * tasaInteresMensual * Math.pow(1 + tasaInteresMensual, numeroPagos)) /
                (Math.pow(1 + tasaInteresMensual, numeroPagos) - 1);

        double totalPagos = pagoMensual * numeroPagos;
        double interesesTotales = totalPagos - monto;

        // Crear el JSON de respuesta simulando el del monolítico
        Map<String,Object> simulacionResultado = new HashMap<>();
        simulacionResultado.put("pagoMensual", pagoMensual);
        simulacionResultado.put("monto", monto);
        simulacionResultado.put("plazo", plazoEnAnios);
        simulacionResultado.put("interesesTotales", interesesTotales);
        simulacionResultado.put("tasaInteres", tasaInteresAnual);
        simulacionResultado.put("totalPagos", totalPagos);

        return simulacionResultado;
    }
}

