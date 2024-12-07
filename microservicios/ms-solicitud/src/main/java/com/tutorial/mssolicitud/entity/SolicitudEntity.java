package com.tutorial.mssolicitud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "solicitud")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Referencia al usuario solicitante (ID proveniente de ms-registro-usuario)
    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "tipo_prestamo")
    private String tipoPrestamo;

    private int plazo; // en años
    @Column(name = "tasa_interes")
    private double tasaInteresAnual;
    private int monto;
    private String estado; // Ej: "En proceso", "Aprobado", "Rechazado"
    @Column(name = "valor_propiedad")
    private int valorPropiedad;
}
