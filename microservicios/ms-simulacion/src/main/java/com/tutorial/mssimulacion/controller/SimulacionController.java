package com.tutorial.mssimulacion.controller;

import com.tutorial.mssimulacion.entity.SimulacionEntity;
import com.tutorial.mssimulacion.service.SimulacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/simulacion")
public class SimulacionController {

    @Autowired
    SimulacionService simulacionService;

    @GetMapping
    public ResponseEntity<List<SimulacionEntity>> getAll() {
        List<SimulacionEntity> simulaciones = simulacionService.getAll();
        if(simulaciones.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(simulaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimulacionEntity> getById(@PathVariable("id") int id) {
        SimulacionEntity simulacion = simulacionService.getSimulacionById(id);
        if(simulacion == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(simulacion);
    }

    @PostMapping
    public ResponseEntity<SimulacionEntity> save(@RequestBody Map<String, Object> request) {
        double monto = ((Number)request.get("monto")).doubleValue();
        int plazo = ((Number)request.get("plazo")).intValue();
        double tasaInteresAnual = ((Number)request.get("tasaInteresAnual")).doubleValue();

        SimulacionEntity simulacionNueva = simulacionService.save(monto, plazo, tasaInteresAnual);
        return ResponseEntity.ok(simulacionNueva);
    }
}
