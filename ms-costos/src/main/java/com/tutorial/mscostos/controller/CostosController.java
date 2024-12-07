package com.tutorial.mscostos.controller;

import com.tutorial.mscostos.entity.CalculoCostosEntity;
import com.tutorial.mscostos.service.CalculoCostosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/costos")
public class CostosController {

    @Autowired
    CalculoCostosService calculoCostosService;

    @GetMapping
    public ResponseEntity<List<CalculoCostosEntity>> getAll() {
        List<CalculoCostosEntity> costos = calculoCostosService.getAll();
        if(costos.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(costos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalculoCostosEntity> getById(@PathVariable("id") int id) {
        CalculoCostosEntity c = calculoCostosService.getById(id);
        if(c == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(c);
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<List<CalculoCostosEntity>> getBySolicitudId(@PathVariable("idSolicitud") int idSolicitud) {
        List<CalculoCostosEntity> costos = calculoCostosService.getBySolicitudId(idSolicitud);
        return ResponseEntity.ok(costos);
    }

    @PostMapping
    public ResponseEntity<CalculoCostosEntity> calcular(@RequestBody Map<String,Object> request) {
        int idSolicitud = ((Number)request.get("idSolicitud")).intValue();
        double montoPrestamo = ((Number)request.get("montoPrestamo")).doubleValue();
        int plazoAnios = ((Number)request.get("plazoAnios")).intValue();
        double tasaInteresAnual = ((Number)request.get("tasaInteresAnual")).doubleValue();

        CalculoCostosEntity nuevo = calculoCostosService.calcularYGuardarCostos(idSolicitud, montoPrestamo, plazoAnios, tasaInteresAnual);
        return ResponseEntity.ok(nuevo);
    }

    @PutMapping
    public ResponseEntity<CalculoCostosEntity> update(@RequestBody CalculoCostosEntity calculo) {
        if(calculo.getId() == 0)
            return ResponseEntity.badRequest().build();
        CalculoCostosEntity actualizado = calculoCostosService.update(calculo);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        boolean deleted = calculoCostosService.deleteById(id);
        if(!deleted)
            return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
