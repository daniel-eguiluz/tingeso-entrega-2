package com.tutorial.mssimulacion.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "simulacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double monto;             // Monto del préstamo
    private int plazo;                // Plazo en años
    private double tasaInteresAnual;  // Tasa de interés anual (%)
    private double cuotaMensual;      // Cuota mensual calculada
}

