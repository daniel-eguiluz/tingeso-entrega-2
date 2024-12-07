package com.tutorial.mssolicitud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "comprobante_ingresos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComprobanteIngresosEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "id")
    private Long id;

    @Column(name = "antiguedad_laboral")
    private int antiguedadLaboral; // años en el empleo actual

    @Column(name = "ingreso_mensual")
    private int ingresoMensual;

    @Column(name = "ingresos_ultimos_24_meses")
    private String ingresosUltimos24Meses;

    @Column(name = "saldo")
    private int saldo; // saldo en cuenta de ahorro o inversiones

    @Column(name = "deudas")
    private int deudas; // deudas actuales

    @Column(name = "cantidad_deudas_pendientes")
    private int cantidadDeudasPendientes; // Número de deudas pendientes

    @Column(name = "saldos_mensuales")
    private String saldosMensuales; // Saldos mensuales de los últimos 12 meses, separados por comas

    @Column(name = "depositos_ultimos_12_meses")
    private String depositosUltimos12Meses; // Montos de los depósitos, separados por comas

    @Column(name = "antiguedad_cuenta")
    private int antiguedadCuenta; // Años de antigüedad de la cuenta de ahorros

    @Column(name = "retiros_ultimos_6_meses")
    private String retirosUltimos6Meses; // Montos de los retiros en los últimos 6 meses, separados por comas
}
