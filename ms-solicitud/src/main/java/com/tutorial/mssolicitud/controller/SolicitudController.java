package com.tutorial.mssolicitud.controller;

import com.tutorial.mssolicitud.entity.ComprobanteIngresosEntity;
import com.tutorial.mssolicitud.entity.PrestamoEntity;
import com.tutorial.mssolicitud.entity.SolicitudEntity;
import com.tutorial.mssolicitud.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/solicitud")
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;

    /**
     * Obtener todas las solicitudes.
     * GET http://localhost:8080/solicitud
     *
     * @return Lista de solicitudes o no content si está vacío.
     */
    @GetMapping
    public ResponseEntity<List<SolicitudEntity>> getAll() {
        List<SolicitudEntity> solicitudes = solicitudService.getAll();
        if (solicitudes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * Obtener una solicitud por su ID.
     * GET http://localhost:8080/solicitud/{id}
     *
     * @param id ID de la solicitud.
     * @return Solicitud encontrada o not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudEntity> getById(@PathVariable("id") int id) {
        SolicitudEntity solicitud = solicitudService.getById(id);
        if (solicitud == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(solicitud);
    }

    /**
     * Obtener la última solicitud de un usuario específico.
     * GET http://localhost:8080/solicitud/usuario/{idUsuario}
     *
     * @param idUsuario ID del usuario.
     * @return Detalles de la última solicitud o not found.
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Map<String, Object>> getSingleSolicitudByUsuarioId(@PathVariable("idUsuario") int idUsuario) {
        List<SolicitudEntity> solicitudes = solicitudService.getByUsuarioId(idUsuario);
        if (solicitudes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Obtener la última solicitud basada en el ID o la fecha (suponiendo que el ID incrementa con el tiempo)
        SolicitudEntity s = solicitudes.get(solicitudes.size() - 1);

        Map<String, Object> response = new HashMap<>();
        response.put("id", s.getId());
        response.put("tipo", s.getTipoPrestamo());          // "tipoPrestamo" se mapea a "tipo"
        response.put("plazo", s.getPlazo());
        response.put("tasaInteres", s.getTasaInteresAnual()); // "tasaInteresAnual" se mapea a "tasaInteres"
        response.put("monto", s.getMonto());
        response.put("estado", s.getEstado());
        response.put("valorPropiedad", s.getValorPropiedad());

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para solicitar un crédito (HU3).
     * POST http://localhost:8080/solicitud/{idUsuario}
     *
     * @param idUsuario ID del usuario que solicita el crédito.
     * @param req       Datos del préstamo y comprobante de ingresos.
     * @return PrestamoEntity creado o bad request en caso de error.
     */
    @PostMapping("/{idUsuario}")
    public ResponseEntity<PrestamoEntity> solicitarCredito(
            @PathVariable("idUsuario") Long idUsuario,
            @RequestBody Map<String, Object> req) {
        try {
            // Extracción de datos para PrestamoEntity
            String tipo = (String) req.get("tipo");
            int plazo = ((Number) req.get("plazo")).intValue();
            double tasaInteres = ((Number) req.get("tasaInteres")).doubleValue();
            int monto = ((Number) req.get("monto")).intValue();
            int valorPropiedad = ((Number) req.get("valorPropiedad")).intValue();

            // Crear PrestamoEntity
            PrestamoEntity prestamo = new PrestamoEntity();
            prestamo.setTipo(tipo);
            prestamo.setPlazo(plazo);
            prestamo.setTasaInteres(tasaInteres);
            prestamo.setMonto(monto);
            prestamo.setEstado("En proceso");
            prestamo.setValorPropiedad(valorPropiedad);

            // Extracción de datos para ComprobanteIngresosEntity
            int antiguedadLaboral = ((Number) req.get("antiguedadLaboral")).intValue();
            int ingresoMensual = ((Number) req.get("ingresoMensual")).intValue();
            int saldo = ((Number) req.get("saldo")).intValue();
            int deudas = ((Number) req.get("deudas")).intValue();
            int cantidadDeudasPendientes = ((Number) req.get("cantidadDeudasPendientes")).intValue();
            int antiguedadCuenta = ((Number) req.get("antiguedadCuenta")).intValue();

            // Extraer listas y convertir a cadenas
            List<Number> ingresosNumbers = (List<Number>) req.get("ingresosUltimos24Meses");
            String ingresosUltimos24Meses = ingresosNumbers.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            List<Number> saldosMensualesNumbers = (List<Number>) req.get("saldosMensuales");
            String saldosMensuales = saldosMensualesNumbers.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            List<Number> depositosNumbers = (List<Number>) req.get("depositosUltimos12Meses");
            String depositosUltimos12Meses = depositosNumbers.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            List<Number> retirosNumbers = (List<Number>) req.get("retirosUltimos6Meses");
            String retirosUltimos6Meses = retirosNumbers.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            // Crear ComprobanteIngresosEntity
            ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
            comprobanteIngresos.setAntiguedadLaboral(antiguedadLaboral);
            comprobanteIngresos.setIngresoMensual(ingresoMensual);
            comprobanteIngresos.setIngresosUltimos24Meses(ingresosUltimos24Meses);
            comprobanteIngresos.setSaldo(saldo);
            comprobanteIngresos.setDeudas(deudas);
            comprobanteIngresos.setCantidadDeudasPendientes(cantidadDeudasPendientes);
            comprobanteIngresos.setSaldosMensuales(saldosMensuales);
            comprobanteIngresos.setDepositosUltimos12Meses(depositosUltimos12Meses);
            comprobanteIngresos.setAntiguedadCuenta(antiguedadCuenta);
            comprobanteIngresos.setRetirosUltimos6Meses(retirosUltimos6Meses);

            // Llamar al servicio para procesar la solicitud de crédito
            PrestamoEntity prestamoSolicitado = solicitudService.solicitarCredito(idUsuario, prestamo, comprobanteIngresos);
            return ResponseEntity.ok(prestamoSolicitado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint para obtener un préstamo por su ID, sin colisionar con getById de SolicitudEntity.
     * GET http://localhost:8080/solicitud/prestamo/{idPrestamo}
     *
     * @param idPrestamo ID del préstamo.
     * @return PrestamoEntity encontrado o not found.
     */
    @GetMapping("/prestamo/{idPrestamo}")
    public ResponseEntity<PrestamoEntity> getPrestamo(@PathVariable Long idPrestamo) {
        try {
            PrestamoEntity prestamo = solicitudService.getPrestamoById(idPrestamo);
            return ResponseEntity.ok(prestamo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para obtener el comprobante de ingresos de un usuario específico.
     * GET http://localhost:8080/solicitud/comprobante/{idUsuario}
     *
     * @param idUsuario ID del usuario.
     * @return ComprobanteIngresosEntity encontrado o not found.
     */
    @GetMapping("/comprobante/{idUsuario}")
    public ResponseEntity<ComprobanteIngresosEntity> getComprobanteByUsuario(@PathVariable("idUsuario") Long idUsuario) {
        try {
            ComprobanteIngresosEntity ci = solicitudService.getComprobanteByUsuario(idUsuario);
            return ResponseEntity.ok(ci);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
