package com.tutorial.mscostos.controller;

import com.tutorial.mscostos.service.CalculoCostosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/costos")
public class CostosController {

    @Autowired
    CalculoCostosService calculoCostosService;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Map<String,Object>> calcularCostos(@PathVariable("idUsuario") Long idUsuario) {
        try {
            Map<String,Object> resultado = calculoCostosService.calcularCostos(idUsuario);
            return ResponseEntity.ok(resultado);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}
