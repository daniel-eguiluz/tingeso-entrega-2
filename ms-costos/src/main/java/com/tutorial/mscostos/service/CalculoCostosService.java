package com.tutorial.mscostos.service;

import com.tutorial.mscostos.entity.CalculoCostosEntity;
import com.tutorial.mscostos.repository.CalculoCostosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculoCostosService {

    @Autowired
    CalculoCostosRepository calculoCostosRepository;

    public List<CalculoCostosEntity> getAll() {
        return calculoCostosRepository.findAll();
    }

    public CalculoCostosEntity getById(int id) {
        return calculoCostosRepository.findById(id).orElse(null);
    }

    public List<CalculoCostosEntity> getBySolicitudId(int idSolicitud) {
        return calculoCostosRepository.findByIdSolicitud(idSolicitud);
    }

    public CalculoCostosEntity calcularYGuardarCostos(int idSolicitud, double montoPrestamo, int plazoAnios, double tasaInteresAnual) {
        // Cálculo de cuota mensual
        double tasaMensual = (tasaInteresAnual / 100.0) / 12.0;
        int numeroPagos = plazoAnios * 12;
        double cuotaMensual = (montoPrestamo * tasaMensual * Math.pow(1 + tasaMensual, numeroPagos))
                / (Math.pow(1 + tasaMensual, numeroPagos) - 1);

        // Cálculo de seguros y comisión (ejemplo fijo)
        double seguroDesgravamenMensual = montoPrestamo * 0.0003; // 0.03% del monto
        double seguroIncendioMensual = 20000.0; // valor fijo, ejemplo
        double comisionAdministracion = montoPrestamo * 0.01; // 1% del monto del préstamo

        double costoMensualTotal = cuotaMensual + seguroDesgravamenMensual + seguroIncendioMensual;
        double costoTotal = (costoMensualTotal * numeroPagos) + comisionAdministracion;

        // Redondear valores
        cuotaMensual = Math.round(cuotaMensual * 100.0) / 100.0;
        seguroDesgravamenMensual = Math.round(seguroDesgravamenMensual * 100.0) / 100.0;
        costoMensualTotal = Math.round(costoMensualTotal * 100.0) / 100.0;
        costoTotal = Math.round(costoTotal * 100.0) / 100.0;
        comisionAdministracion = Math.round(comisionAdministracion * 100.0) / 100.0;

        CalculoCostosEntity c = new CalculoCostosEntity();
        c.setIdSolicitud(idSolicitud);
        c.setMontoPrestamo(montoPrestamo);
        c.setPlazoAnios(plazoAnios);
        c.setTasaInteresAnual(tasaInteresAnual);
        c.setCuotaMensual(cuotaMensual);
        c.setSeguroDesgravamenMensual(seguroDesgravamenMensual);
        c.setSeguroIncendioMensual(seguroIncendioMensual);
        c.setComisionAdministracion(comisionAdministracion);
        c.setCostoMensualTotal(costoMensualTotal);
        c.setCostoTotal(costoTotal);
        c.setNumeroPagos(numeroPagos);

        return calculoCostosRepository.save(c);
    }

    public CalculoCostosEntity update(CalculoCostosEntity calculo) {
        return calculoCostosRepository.save(calculo);
    }

    public boolean deleteById(int id) {
        if(calculoCostosRepository.existsById(id)) {
            calculoCostosRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
