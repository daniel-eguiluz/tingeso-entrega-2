package com.tutorial.msevaluacion.controller;

import com.tutorial.msevaluacion.entity.EvaluacionEntity;
import com.tutorial.msevaluacion.service.EvaluacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/evaluacion")
public class EvaluacionController {

    @Autowired
    EvaluacionService evaluacionService;

    @GetMapping
    public ResponseEntity<List<EvaluacionEntity>> getAll() {
        List<EvaluacionEntity> evaluaciones = evaluacionService.getAll();
        if(evaluaciones.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(evaluaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluacionEntity> getById(@PathVariable("id") int id) {
        EvaluacionEntity eval = evaluacionService.getById(id);
        if(eval == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(eval);
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<List<EvaluacionEntity>> getBySolicitudId(@PathVariable("idSolicitud") int idSolicitud) {
        List<EvaluacionEntity> evaluaciones = evaluacionService.getBySolicitudId(idSolicitud);
        return ResponseEntity.ok(evaluaciones);
    }

    @PostMapping
    public ResponseEntity<EvaluacionEntity> save(@RequestBody Map<String, Object> request) {
        int idSolicitud = ((Number)request.get("idSolicitud")).intValue();
        String resultado = (String) request.get("resultado");
        String observaciones = (String) request.get("observaciones");

        EvaluacionEntity nueva = evaluacionService.save(idSolicitud, resultado, observaciones);
        return ResponseEntity.ok(nueva);
    }

    @PutMapping
    public ResponseEntity<EvaluacionEntity> update(@RequestBody EvaluacionEntity evaluacion) {
        if(evaluacion.getId() == 0)
            return ResponseEntity.badRequest().build();
        EvaluacionEntity actualizada = evaluacionService.update(evaluacion);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        boolean deleted = evaluacionService.deleteById(id);
        if(!deleted)
            return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
