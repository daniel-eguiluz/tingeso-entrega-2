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
public class CalculoCostosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_solicitud")
    private int idSolicitud;

    private double montoPrestamo;
    private int plazoAnios;
    private double tasaInteresAnual;
    private double cuotaMensual;
    private double seguroDesgravamenMensual;
    private double seguroIncendioMensual;
    private double comisionAdministracion;
    private double costoMensualTotal;
    private double costoTotal;
    private int numeroPagos;
}
