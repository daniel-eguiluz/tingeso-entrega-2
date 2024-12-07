package com.tutorial.mssolicitud.controller;

import com.tutorial.mssolicitud.entity.SolicitudEntity;
import com.tutorial.mssolicitud.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitud")
public class SolicitudController {

    @Autowired
    SolicitudService solicitudService;

    @GetMapping
    public ResponseEntity<List<SolicitudEntity>> getAll() {
        List<SolicitudEntity> solicitudes = solicitudService.getAll();
        if(solicitudes.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudEntity> getById(@PathVariable("id") int id) {
        SolicitudEntity solicitud = solicitudService.getById(id);
        if(solicitud == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(solicitud);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<SolicitudEntity>> getByUsuarioId(@PathVariable("idUsuario") int idUsuario) {
        List<SolicitudEntity> solicitudes = solicitudService.getByUsuarioId(idUsuario);
        return ResponseEntity.ok(solicitudes);
    }

    @PostMapping
    public ResponseEntity<SolicitudEntity> save(@RequestBody SolicitudEntity solicitud) {
        SolicitudEntity nueva = solicitudService.save(solicitud);
        return ResponseEntity.ok(nueva);
    }

    @PutMapping
    public ResponseEntity<SolicitudEntity> update(@RequestBody SolicitudEntity solicitud) {
        if(solicitud.getId() == 0)
            return ResponseEntity.badRequest().build();
        SolicitudEntity actualizada = solicitudService.update(solicitud);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        boolean deleted = solicitudService.deleteById(id);
        if(!deleted)
            return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
