package com.example.demo.pagos.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.pagos.dto.CompraDTO;
import com.example.demo.pagos.entity.Compra;
import com.example.demo.pagos.repository.PagoRepository;

@Service
@Transactional
public class PagoService {

    @Autowired
    PagoRepository pagoRepository;

    public Optional<Compra> findByIdCompra(int idCompra) {
        return pagoRepository.findByIdCompra(idCompra);
    }

    public boolean existsByIdCompra(int idCompra) {
        return pagoRepository.existsByIdCompra(idCompra);
    }

    public List<Compra> findByIdUsuario(int idUsuario) {
        return pagoRepository.findByIdUsuario(idUsuario);
    }

    public List<Compra> findByEstadoCompra(String estado) {
        return pagoRepository.findByEstadoCompra(estado);
    }

    public void save(Compra compra) {
        pagoRepository.save(compra);
    }

    public long cantidadCompras() {
        return pagoRepository.count();
    }

}