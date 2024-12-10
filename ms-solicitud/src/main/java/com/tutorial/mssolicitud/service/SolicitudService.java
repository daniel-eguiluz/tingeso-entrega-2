package com.tutorial.mssolicitud.service;

import com.tutorial.mssolicitud.entity.ComprobanteIngresosEntity;
import com.tutorial.mssolicitud.entity.PrestamoEntity;
import com.tutorial.mssolicitud.entity.SolicitudEntity;
import com.tutorial.mssolicitud.entity.UsuarioComprobanteIngresosEntity;
import com.tutorial.mssolicitud.entity.UsuarioPrestamoEntity;
import com.tutorial.mssolicitud.repository.ComprobanteIngresosRepository;
import com.tutorial.mssolicitud.repository.PrestamoRepository;
import com.tutorial.mssolicitud.repository.SolicitudRepository;
import com.tutorial.mssolicitud.repository.UsuarioComprobanteIngresosRepository;
import com.tutorial.mssolicitud.repository.UsuarioPrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SolicitudService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private UsuarioPrestamoRepository usuarioPrestamoRepository;

    @Autowired
    private ComprobanteIngresosRepository comprobanteIngresosRepository;

    @Autowired
    private UsuarioComprobanteIngresosRepository usuarioComprobanteIngresosRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SolicitudRepository solicitudRepository;

    public List<SolicitudEntity> getAll() {
        return solicitudRepository.findAll();
    }

    public SolicitudEntity getById(int id) {
        return solicitudRepository.findById(id).orElse(null);
    }

    public List<SolicitudEntity> getByUsuarioId(int idUsuario) {
        return solicitudRepository.findByIdUsuario(idUsuario);
    }

    public SolicitudEntity save(SolicitudEntity solicitud) {
        // Establecer un estado inicial, por ejemplo "En proceso"
        if (solicitud.getEstado() == null || solicitud.getEstado().isEmpty()) {
            solicitud.setEstado("En proceso");
        }
        return solicitudRepository.save(solicitud);
    }

    public SolicitudEntity update(SolicitudEntity solicitud) {
        // Se asume que la solicitud ya existe
        return solicitudRepository.save(solicitud);
    }

    public boolean deleteById(int id) {
        if (solicitudRepository.existsById(id)) {
            solicitudRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public PrestamoEntity solicitarCredito(Long idUsuario, PrestamoEntity prestamo, ComprobanteIngresosEntity comprobante) throws Exception {
        // Verificar usuario en ms-usuario
        Map<String, Object> usuario = restTemplate.getForObject("http://ms-usuario/usuario/" + idUsuario, Map.class);
        if (usuario == null) {
            throw new Exception("Usuario no encontrado");
        }

        prestamo.setEstado("En proceso");
        PrestamoEntity prestamoGuardado = prestamoRepository.save(prestamo);

        ComprobanteIngresosEntity ciGuardado = comprobanteIngresosRepository.save(comprobante);

        UsuarioPrestamoEntity up = new UsuarioPrestamoEntity();
        up.setIdUsuario(idUsuario);
        up.setIdPrestamo(prestamoGuardado.getId());
        usuarioPrestamoRepository.save(up);

        UsuarioComprobanteIngresosEntity uci = new UsuarioComprobanteIngresosEntity();
        uci.setIdUsuario(idUsuario);
        uci.setIdComprobanteIngresos(ciGuardado.getId());
        usuarioComprobanteIngresosRepository.save(uci);

        // Crear o guardar la solicitud con los datos proporcionados.
        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setIdUsuario(idUsuario.intValue());
        solicitud.setTipoPrestamo(prestamo.getTipo());
        solicitud.setPlazo(prestamo.getPlazo());
        solicitud.setTasaInteresAnual(prestamo.getTasaInteres());
        solicitud.setMonto(prestamo.getMonto());
        solicitud.setEstado("En proceso");
        solicitud.setValorPropiedad(prestamo.getValorPropiedad());
        solicitud.setIdPrestamo(prestamoGuardado.getId()); // ASIGNAR EL ID DEL PRESTAMO

        solicitudRepository.save(solicitud);

        return prestamoGuardado;
    }

    public PrestamoEntity getPrestamoById(Long id) throws Exception {
        return prestamoRepository.findById(id).orElseThrow(() -> new Exception("Prestamo no encontrado"));
    }

    public ComprobanteIngresosEntity getComprobanteByUsuario(Long idUsuario) throws Exception {
        UsuarioComprobanteIngresosEntity uci = usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("No se encontró comprobante de ingresos para el usuario " + idUsuario));
        return comprobanteIngresosRepository.findById(uci.getIdComprobanteIngresos())
                .orElseThrow(() -> new Exception("No se encontró el comprobante de ingresos con id " + uci.getIdComprobanteIngresos()));
    }
}
