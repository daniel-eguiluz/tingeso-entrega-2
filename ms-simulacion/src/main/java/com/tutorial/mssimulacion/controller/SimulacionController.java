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

    // Cambiamos el endpoint a GET y la ruta para que quede como el monolítico:
    @GetMapping("/{idUsuario}")
    public ResponseEntity<Map<String,Object>> simularCredito(@PathVariable("idUsuario") Long idUsuario) {
        try {
            Map<String,Object> resultado = simulacionService.simularCredito(idUsuario);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
