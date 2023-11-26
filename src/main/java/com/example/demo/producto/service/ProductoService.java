package com.example.demo.producto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.producto.entity.Producto;
import com.example.demo.producto.repository.ProductoRepository;
import com.example.demo.security.repository.UsuarioRepository;

@Service
@Transactional
public class ProductoService {

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    public List<Producto> list() {
        List<String> listaUsernames = usuarioRepository.findUsernameByActivoIsTrue();
        return productoRepository.findByCantidadNotAndVendedor_UsernameInAndActivoIsTrue(0, listaUsernames);
    }

    public List<Producto> list(String vendedor) {
        return productoRepository.findByVendedor(vendedor);
    }

    public Optional<Producto> getOne(int id) {
        return productoRepository.findById(id);
    }

    public Optional<Producto> getByNombre(String nombre) {
        return productoRepository.findByNombre(nombre);
    }

    public void save(Producto producto) {
        productoRepository.save(producto);
    }

    public void delete(int id) {
        productoRepository.deleteById(id);
    }

    public boolean existsById(int id) {
        return productoRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return productoRepository.existsByNombre(nombre);
    }
}