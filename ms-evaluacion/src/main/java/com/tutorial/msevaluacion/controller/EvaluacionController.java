package com.tutorial.msevaluacion.controller;

import com.tutorial.msevaluacion.service.EvaluacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/evaluacion")
public class EvaluacionController {

    private static final Logger logger = LoggerFactory.getLogger(EvaluacionController.class);

    @Autowired
    private EvaluacionService evaluacionService;

    /**
     * Endpoint para evaluar el crédito basado en el ID de la solicitud.
     *
     * @param idSolicitud El ID de la solicitud a evaluar.
     * @return Una respuesta HTTP con el resultado de la evaluación.
     */
    @GetMapping("/{idSolicitud}")
    public ResponseEntity<Map<String, Object>> evaluarCredito(@PathVariable("idSolicitud") int idSolicitud) {
        logger.info("Recibida solicitud de evaluación para ID de Solicitud: {}", idSolicitud);
        try {
            Map<String, Object> resultado = evaluacionService.evaluarCredito(idSolicitud);
            logger.info("Evaluación completada para ID de Solicitud: {}", idSolicitud);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            logger.error("Error al evaluar crédito para ID de Solicitud {}: {}", idSolicitud, e.getMessage());
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}
