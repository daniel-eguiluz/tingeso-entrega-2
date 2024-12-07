package com.tutorial.msevaluacion.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "evaluacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_solicitud")
    private int idSolicitud;

    private String resultado; // "Aprobado" o "Rechazado"
    private String observaciones;

    @Column(name = "fecha_evaluacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEvaluacion;
}

