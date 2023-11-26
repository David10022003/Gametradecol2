package com.example.demo.pagos.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import jakarta.persistence.Column;

public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCompra;

    @NotNull
    private int idUsuario;

    @NotNull
    private int precioCompra;

    @NotNull
    @NotBlank
    private String estadoCompra;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    
}
