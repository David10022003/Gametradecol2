package com.example.demo.producto.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class ProductoDto {

    @NotBlank
    private String nombre;
    @Min(100)
    private Float precio;
    private String imagen;
    private String descripcion;
    private String vendedor;
    private Integer cantidad;

    public ProductoDto() {
    }

    public ProductoDto(@NotBlank String nombre, @Min(100) Float precio, String imagen, String descripcion,
            String vendedor, Integer cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.vendedor = vendedor;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendendor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}