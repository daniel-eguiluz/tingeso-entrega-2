package com.tutorial.mssolicitud.controller;

import com.tutorial.mssolicitud.entity.ComprobanteIngresosEntity;
import com.tutorial.mssolicitud.entity.PrestamoEntity;
import com.tutorial.mssolicitud.entity.SolicitudEntity;
import com.tutorial.mssolicitud.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    @PostMapping
    public ResponseEntity<PrestamoEntity> solicitar(@RequestBody Map<String,Object> req) {
        try {
            Long idUsuario = ((Number)req.get("idUsuario")).longValue();
            PrestamoEntity p = new PrestamoEntity();
            p.setTipo((String)req.get("tipo"));
            p.setPlazo(((Number)req.get("plazo")).intValue());
            p.setTasaInteres(((Number)req.get("tasaInteres")).doubleValue());
            p.setMonto(((Number)req.get("monto")).intValue());
            p.setValorPropiedad(((Number)req.get("valorPropiedad")).intValue());

            ComprobanteIngresosEntity c = new ComprobanteIngresosEntity();
            c.setAntiguedadLaboral(((Number)req.get("antiguedadLaboral")).intValue());
            c.setIngresoMensual(((Number)req.get("ingresoMensual")).intValue());
            c.setSaldosMensuales(convertListToString((List<Number>)req.get("saldosMensuales")));
            // ... y demás campos

            PrestamoEntity guardado = solicitudService.solicitarCredito(idUsuario,p,c);
            return ResponseEntity.ok(guardado);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoEntity> getPrestamo(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(solicitudService.getPrestamoById(id));
        } catch(Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String convertListToString(List<Number> list) {
        return list.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

}
