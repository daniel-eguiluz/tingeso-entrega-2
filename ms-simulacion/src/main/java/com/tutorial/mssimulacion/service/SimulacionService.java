package com.tutorial.mssimulacion.service;

import com.tutorial.mssimulacion.entity.SimulacionEntity;
import com.tutorial.mssimulacion.repository.SimulacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class SimulacionService {

    @Autowired
    SimulacionRepository simulacionRepository;

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate; // Para llamar a otros MS

    public SimulacionEntity simular(Long idUsuario, Long idPrestamo) throws Exception {
        // Llamar a ms-solicitud-credito para obtener datos del prestamo
        Map prestamo = restTemplate.getForObject("http://ms-solicitud-credito/solicitud/"+idPrestamo, Map.class);
        if(prestamo == null) {
            throw new Exception("Prestamo no encontrado");
        }

        double tasa = ((Number)prestamo.get("tasaInteres")).doubleValue();
        int plazo = ((Number)prestamo.get("plazo")).intValue();
        int monto = ((Number)prestamo.get("monto")).intValue();

        double tasaMensual = (tasa/100)/12;
        int numeroPagos = plazo * 12;
        double cuota = (monto * tasaMensual * Math.pow(1+tasaMensual, numeroPagos)) /
                (Math.pow(1+tasaMensual, numeroPagos)-1);

        SimulacionEntity s = new SimulacionEntity();
        s.setIdUsuario(idUsuario);
        s.setIdPrestamo(idPrestamo);
        s.setMonto(monto);
        s.setPlazo(plazo);
        s.setTasaInteresAnual(tasa);
        s.setCuotaMensual(Math.round(cuota*100.0)/100.0);
        return simulacionRepository.save(s);
    }

    public List<SimulacionEntity> getAll() {
        return simulacionRepository.findAll();
    }
}

