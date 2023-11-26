package com.example.demo.producto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.access.prepost.PreAuthorize;

import org.apache.commons.lang3.StringUtils;

import com.example.demo.producto.service.ProductoService;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.security.service.UsuarioService;
import com.example.demo.producto.entity.Producto;
import com.example.demo.producto.dto.Mensaje;
import com.example.demo.producto.dto.ProductoDto;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/producto")
@CrossOrigin
public class ProductoController {

    @Autowired
    ProductoService productoService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/lista")
    public ResponseEntity<List<Producto>> list() {
        List<Producto> list = productoService.list();
        return new ResponseEntity<List<Producto>>(list, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    @GetMapping("/lista/{vendedor}")
    public ResponseEntity<List<Producto>> list(@PathVariable("vendedor") String vendedor) {
        List<Producto> list = productoService.list(vendedor);
        return new ResponseEntity<List<Producto>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Producto> getById(@PathVariable("id") int id) {
        if (!productoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Producto producto = productoService.getOne(id).get();
        return new ResponseEntity<Producto>(producto, HttpStatus.OK);
    }

    @GetMapping("/detailname/{nombre}")
    public ResponseEntity<Producto> getByNombre(@PathVariable("nombre") String nombre) {
        if (!productoService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Producto producto = productoService.getByNombre(nombre).get();

        return new ResponseEntity<Producto>(producto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProductoDto productoDto) {
        if (StringUtils.isBlank(productoDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre debe tener al menos 5 caracteres"),
                    HttpStatus.BAD_REQUEST);
        if (productoDto.getPrecio() == null || productoDto.getPrecio() < 100)
            return new ResponseEntity(new Mensaje("el precio debe ser mayor que 100"), HttpStatus.BAD_REQUEST);
        if (productoService.existsByNombre(productoDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);
        if (productoDto.getPrecio() == null || productoDto.getCantidad() <= 0)
            return new ResponseEntity(new Mensaje("la cantidad debe ser mayor a 0"), HttpStatus.BAD_REQUEST);
        Producto producto = new Producto(productoDto.getNombre(), productoDto.getPrecio(),
                productoDto.getImagen(), productoDto.getDescripcion(), productoDto.getVendedor(),
                productoDto.getCantidad());
        productoService.save(producto);
        return new ResponseEntity(new Mensaje("producto creado"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody ProductoDto productoDto) {
        if (!productoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        if (productoService.existsByNombre(productoDto.getNombre())
                && productoService.getByNombre(productoDto.getNombre()).get().getId() != id)
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(productoDto.getNombre()) || productoDto.getNombre().length() < 5)
            return new ResponseEntity(new Mensaje("el nombre debe tener al menos 5 caracteres"),
                    HttpStatus.BAD_REQUEST);
        if (productoDto.getPrecio() == null || productoDto.getPrecio() < 100)
            return new ResponseEntity(new Mensaje("el precio debe ser mayor que 100"), HttpStatus.BAD_REQUEST);
        if (productoDto.getCantidad() == null || productoDto.getCantidad() <= 0)
            return new ResponseEntity(new Mensaje("la cantidad debe ser mayor a 0"), HttpStatus.BAD_REQUEST);
        if (productoDto.getImagen() == null)
            return new ResponseEntity(new Mensaje("es necesaria una imagen"), HttpStatus.BAD_REQUEST);

        Producto producto = productoService.getOne(id).get();
        producto.setNombre(productoDto.getNombre());
        producto.setPrecio(productoDto.getPrecio());
        producto.setDescripcion(productoDto.getDescripcion());
        producto.setCantidad(productoDto.getCantidad());
        producto.setImagen(productoDto.getImagen());
        productoService.save(producto);
        return new ResponseEntity(new Mensaje("producto actualizado"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        if (!productoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        productoService.delete(id);
        return new ResponseEntity(new Mensaje("producto eliminado"), HttpStatus.OK);
    }
}