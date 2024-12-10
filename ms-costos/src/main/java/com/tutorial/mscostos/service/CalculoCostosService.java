package com.tutorial.mscostos.service;

import com.tutorial.mscostos.entity.CalculoCostosEntity;
import com.tutorial.mscostos.repository.CalculoCostosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalculoCostosService {

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

    @Autowired
    CalculoCostosRepository calculoCostosRepository;

    public List<CalculoCostosEntity> getAll() {
        return calculoCostosRepository.findAll();
    }

    public CalculoCostosEntity getById(int id) {
        return calculoCostosRepository.findById(id).orElse(null);
    }

    public List<CalculoCostosEntity> getByUsuarioId(Long idUsuario) {
        return calculoCostosRepository.findByIdUsuario(idUsuario);
    }

    /**
     * Obtiene datos del préstamo asociando el idUsuario a través de ms-solicitud.
     * Se asume que ms-solicitud tiene un endpoint:
     * GET http://ms-solicitud/solicitud/usuario/{idUsuario}
     * que retorna un JSON con al menos: "monto", "plazo" y "tasaInteres".
     */
    public Map<String,Object> calcularCostos(Long idUsuario) throws Exception {
        // Obtener datos del préstamo desde ms-solicitud
        Map prestamo = restTemplate.getForObject("http://ms-solicitud/solicitud/usuario/" + idUsuario, Map.class);
        if(prestamo == null) throw new Exception("No se encontraron datos de préstamo para el usuario " + idUsuario);

        double monto = ((Number)prestamo.get("monto")).doubleValue();
        int plazo = ((Number)prestamo.get("plazo")).intValue();
        double tasa = ((Number)prestamo.get("tasaInteres")).doubleValue();

        // Cálculo de costos
        double tasaMensual = (tasa / 100.0) / 12.0;
        int numeroPagos = plazo * 12;
        double cuotaMensual = (monto * tasaMensual * Math.pow(1 + tasaMensual, numeroPagos))
                / (Math.pow(1 + tasaMensual, numeroPagos) - 1);

        double seguroDesgravamenMensual = monto * 0.0003;
        double seguroIncendioMensual = 20000.0;
        double comisionAdministracion = monto * 0.01;
        double costoMensualTotal = cuotaMensual + seguroDesgravamenMensual + seguroIncendioMensual;
        double costoTotal = (costoMensualTotal * numeroPagos) + comisionAdministracion;

        // Redondear
        cuotaMensual = Math.round(cuotaMensual * 100.0) / 100.0;
        seguroDesgravamenMensual = Math.round(seguroDesgravamenMensual * 100.0) / 100.0;
        costoMensualTotal = Math.round(costoMensualTotal * 100.0) / 100.0;
        costoTotal = Math.round(costoTotal * 100.0) / 100.0;
        comisionAdministracion = Math.round(comisionAdministracion * 100.0) / 100.0;

        // Guardar en base de datos
        CalculoCostosEntity c = new CalculoCostosEntity();
        c.setIdUsuario(idUsuario);
        c.setMontoPrestamo(monto);
        c.setPlazoAnios(plazo);
        c.setTasaInteresAnual(tasa);
        c.setCuotaMensual(cuotaMensual);
        c.setSeguroDesgravamenMensual(seguroDesgravamenMensual);
        c.setSeguroIncendioMensual(seguroIncendioMensual);
        c.setComisionAdministracion(comisionAdministracion);
        c.setCostoMensualTotal(costoMensualTotal);
        c.setCostoTotal(costoTotal);
        c.setNumeroPagos(numeroPagos);

        calculoCostosRepository.save(c);

        Map<String,Object> resultado = new HashMap<>();
        resultado.put("seguroIncendioMensual", seguroIncendioMensual);
        resultado.put("tasaInteresAnual", tasa);
        resultado.put("montoPrestamo", monto);
        resultado.put("costoTotal", costoTotal);
        resultado.put("costoMensualTotal", costoMensualTotal);
        resultado.put("plazoAnios", plazo);
        resultado.put("numeroPagos", numeroPagos);
        resultado.put("tasaInteresMensual", tasaMensual * 100);
        resultado.put("cuotaMensual", cuotaMensual);
        resultado.put("comisionAdministracion", comisionAdministracion);
        resultado.put("seguroDesgravamenMensual", seguroDesgravamenMensual);

        return resultado;
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
