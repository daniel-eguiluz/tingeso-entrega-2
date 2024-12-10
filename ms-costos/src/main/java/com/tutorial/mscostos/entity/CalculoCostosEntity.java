package com.tutorial.mscostos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "calculo_costos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculoCostosEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "id")
    private int id;

    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "monto_prestamo")
    private double montoPrestamo;

    @Column(name = "plazo_anios")
    private int plazoAnios;

    @Column(name = "tasa_interes_anual")
    private double tasaInteresAnual;

    @Column(name = "cuota_mensual")
    private double cuotaMensual;

    @Column(name = "seguro_desgravamen_mensual")
    private double seguroDesgravamenMensual;

    @Column(name = "seguro_incendio_mensual")
    private double seguroIncendioMensual;

    @Column(name = "comision_administracion")
    private double comisionAdministracion;

    @Column(name = "costo_mensual_total")
    private double costoMensualTotal;

    @Column(name = "costo_total")
    private double costoTotal;

    @Column(name = "numero_pagos")
    private int numeroPagos;
}
