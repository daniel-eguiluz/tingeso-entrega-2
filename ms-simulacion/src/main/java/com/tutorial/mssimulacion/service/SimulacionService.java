package com.tutorial.mssimulacion.service;

import com.tutorial.mssimulacion.entity.SimulacionEntity;
import com.tutorial.mssimulacion.repository.SimulacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimulacionService {

    @Autowired
    SimulacionRepository simulacionRepository;

    public List<SimulacionEntity> getAll() {
        return simulacionRepository.findAll();
    }

    public SimulacionEntity getSimulacionById(int id) {
        return simulacionRepository.findById(id).orElse(null);
    }

    public SimulacionEntity save(double monto, int plazo, double tasaAnual) {
        // Cálculo de la cuota mensual
        double tasaMensual = (tasaAnual / 100.0) / 12.0;
        int numeroPagos = plazo * 12;
        double cuota = (monto * tasaMensual * Math.pow(1 + tasaMensual, numeroPagos))
                / (Math.pow(1 + tasaMensual, numeroPagos) - 1);

        // Redondear a 2 decimales (opcional)
        double cuotaRedondeada = Math.round(cuota * 100.0) / 100.0;

        SimulacionEntity s = new SimulacionEntity();
        s.setMonto(monto);
        s.setPlazo(plazo);
        s.setTasaInteresAnual(tasaAnual);
        s.setCuotaMensual(cuotaRedondeada);

        return simulacionRepository.save(s);
    }
}
