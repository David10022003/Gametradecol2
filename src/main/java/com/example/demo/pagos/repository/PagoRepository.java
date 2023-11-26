package com.example.demo.pagos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.pagos.dto.CompraDTO;
import com.example.demo.pagos.entity.Compra;

@Repository
@Qualifier("compra")
public interface PagoRepository extends JpaRepository<Compra, Integer> {

    boolean existsByIdCompra(int idCompra);

    List<Compra> findByIdUsuario(int idUsuario);

    Optional<Compra> findByIdCompra(int idCompra);

    List<Compra> findByEstadoCompra(String estadoCompra);

    long count();

}
