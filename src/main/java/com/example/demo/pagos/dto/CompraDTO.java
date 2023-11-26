package com.example.demo.pagos.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;

import com.example.demo.producto.entity.Producto;

public class CompraDTO {

    private int idCompra;

    @NotNull(message = "Usuario no existe")
    private String idUsuario;

    @NotNull(message = "Precio no puede ser nulo")
    private int precio;

    private String estadoCompra;

    private List<Producto> productos = new ArrayList<>();

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getEstadoCompra() {
        return estadoCompra;
    }

    public void setEstadoCompra(String estadoCompra) {
        this.estadoCompra = estadoCompra;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    /*
     * private Set<int[]> deserializeIntArraySet(String serializedIntArraySet) {
     * Set<int[]> intArraySet = new HashSet<>();
     * String[] arrayStrings = serializedIntArraySet.split(";");
     * for (String arrayString : arrayStrings) {
     * int[] intArray = Arrays.stream(arrayString.split(","))
     * .mapToInt(Integer::parseInt)
     * .toArray();
     * intArraySet.add(intArray);
     * }
     * return intArraySet;
     * }
     */

}
