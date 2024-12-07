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

    @PostMapping
    public ResponseEntity<SimulacionEntity> simular(@RequestBody Map<String,Object> req) {
        try {
            Long idUsuario = ((Number)req.get("idUsuario")).longValue();
            Long idPrestamo = ((Number)req.get("idPrestamo")).longValue();
            SimulacionEntity sim = simulacionService.simular(idUsuario, idPrestamo);
            return ResponseEntity.ok(sim);
        } catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<SimulacionEntity>> getAll() {
        return ResponseEntity.ok(simulacionService.getAll());
    }
}

