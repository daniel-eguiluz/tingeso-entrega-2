package com.tutorial.mssolicitud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "prestamo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "id")
    private Long id;

    @Column(name = "tipo")// entre "Primera vivienda", "Segunda vivienda", "Propiedades comerciales", "Remodelacion"
    private String tipo;

    @Column(name = "plazo")
    private int plazo;

    @Column(name = "tasa_interes")
    private double tasaInteres;

    @Column(name = "monto")
    private int monto;

    @Column(name = "estado")// entre "Aprobado", "Rechazado", "En proceso"
    private String estado;

    @Column(name = "valor_propiedad")
    private int valorPropiedad; // Valor de la propiedad
}
