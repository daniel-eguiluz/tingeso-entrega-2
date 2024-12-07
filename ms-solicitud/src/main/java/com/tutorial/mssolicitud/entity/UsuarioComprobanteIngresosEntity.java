package com.tutorial.mssolicitud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "usuario_comprobante_ingresos")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioComprobanteIngresosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "id")
    private Long id;

    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "id_comprobante_ingresos")
    private Long idComprobanteIngresos;
}
