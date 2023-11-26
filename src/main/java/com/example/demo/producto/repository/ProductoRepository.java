package com.example.demo.producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.producto.entity.Producto;

import java.util.Optional;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Optional<Producto> findByNombre(String nombre);

    List<Producto> findByVendedor(String vendedor);

    boolean existsByNombre(String nombre);

    List<Producto> findByCantidadNot(Integer cantidad);

    @Query("SELECT p FROM Producto p JOIN Usuario u ON p.vendedor = u.username " +
            "WHERE p.cantidad <> :cantidad AND u.username IN :usernames AND u.activo = true")
    List<Producto> findByCantidadNotAndVendedor_UsernameInAndActivoIsTrue(
            @Param("cantidad") Integer cantidad,
            @Param("usernames") List<String> usernames);
}