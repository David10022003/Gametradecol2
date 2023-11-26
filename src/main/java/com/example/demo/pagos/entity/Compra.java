package com.example.demo.pagos.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.persistence.JoinColumn;

import com.example.demo.producto.entity.Producto;

import jakarta.persistence.Column;

@Entity
public class Compra {

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

    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "comprasUsuario", joinColumns = @JoinColumn(name = "idCompra"), inverseJoinColumns = {
            @JoinColumn(name = "idItem") })
    private List<Producto> compras = new ArrayList<>();

    public Compra() {
    }

    public Compra(int idCompra, @NotNull int idUsuario, @NotNull int precioCompra,
            @NotNull @NotBlank String estadoCompra, @NotNull List<Producto> compras) {
        this.idCompra = idCompra;
        this.idUsuario = idUsuario;
        this.precioCompra = precioCompra;
        this.estadoCompra = estadoCompra;
        this.compras = compras;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(int precioCompra) {
        this.precioCompra = precioCompra;
    }

    public String getEstadoCompra() {
        return estadoCompra;
    }

    public void setEstadoCompra(String estadoCompra) {
        this.estadoCompra = estadoCompra;
    }

    public List<Producto> getProductos() {
        return compras;
    }

    public void setProductos(List<Producto> compras) {
        this.compras = compras;
    }

}
