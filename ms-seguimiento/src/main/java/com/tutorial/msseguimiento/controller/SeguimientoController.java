package com.tutorial.msseguimiento.controller;

import com.tutorial.msseguimiento.service.SeguimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/seguimiento")
public class SeguimientoController {

    @Autowired
    SeguimientoService seguimientoService;

    // Ahora recibimos idUsuario en lugar de idSolicitud
    @GetMapping("/{idUsuario}")
    public ResponseEntity<Map<String,Object>> obtenerEstadoPorUsuario(@PathVariable("idUsuario") Long idUsuario) {
        Map<String,Object> datos = seguimientoService.obtenerDatosPrestamoPorUsuario(idUsuario);
        if (datos == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(datos);
    }
}