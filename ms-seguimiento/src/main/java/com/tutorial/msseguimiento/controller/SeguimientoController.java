package com.tutorial.msseguimiento.controller;

import com.tutorial.msseguimiento.entity.SeguimientoEntity;
import com.tutorial.msseguimiento.service.SeguimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/seguimiento")
public class SeguimientoController {

    @Autowired
    SeguimientoService seguimientoService;

    @GetMapping
    public ResponseEntity<List<SeguimientoEntity>> getAll() {
        List<SeguimientoEntity> seguimientos = seguimientoService.getAll();
        if(seguimientos.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(seguimientos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeguimientoEntity> getById(@PathVariable("id") int id) {
        SeguimientoEntity seg = seguimientoService.getById(id);
        if(seg == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(seg);
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<List<SeguimientoEntity>> getBySolicitudId(@PathVariable("idSolicitud") int idSolicitud) {
        List<SeguimientoEntity> seguimientos = seguimientoService.getBySolicitudId(idSolicitud);
        return ResponseEntity.ok(seguimientos);
    }

    @PostMapping
    public ResponseEntity<SeguimientoEntity> save(@RequestBody Map<String, Object> request) {
        int idSolicitud = ((Number)request.get("idSolicitud")).intValue();
        String estadoActual = (String) request.get("estadoActual");
        SeguimientoEntity nuevo = seguimientoService.save(idSolicitud, estadoActual);
        return ResponseEntity.ok(nuevo);
    }

    @PutMapping
    public ResponseEntity<SeguimientoEntity> update(@RequestBody SeguimientoEntity seguimiento) {
        if(seguimiento.getId() == 0)
            return ResponseEntity.badRequest().build();
        SeguimientoEntity actualizado = seguimientoService.update(seguimiento);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        boolean deleted = seguimientoService.deleteById(id);
        if(!deleted)
            return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
