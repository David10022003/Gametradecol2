package com.example.demo.security.controller;

import java.util.List;

import com.example.demo.security.dto.UserDto;
import com.example.demo.security.entity.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.security.service.UsuarioService;
import com.example.demo.producto.dto.Mensaje;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    UsuarioService usuarioService;

    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<?> listaUsers() {
        List<Usuario> lista = usuarioService.getAll();
        return new ResponseEntity<List<Usuario>>(lista, HttpStatus.OK);
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/desactivar")
    public ResponseEntity<?> desactivar(@PathVariable("id") int id) {
        if (!usuarioService.existsById(id))
            return new ResponseEntity(new Mensaje("ese usuario no existe"), HttpStatus.BAD_REQUEST);
        Usuario usuario = usuarioService.getOne(id).get();
        usuario.setActivo(false);
        usuarioService.save(usuario);
        return new ResponseEntity(new Mensaje("Usuario inhabilitado"), HttpStatus.OK);
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/activar")
    public ResponseEntity<?> activar(@PathVariable("id") int id) {
        if (!usuarioService.existsById(id))
            return new ResponseEntity(new Mensaje("ese usuario no existe"), HttpStatus.BAD_REQUEST);
        Usuario usuario = usuarioService.getOne(id).get();
        usuario.setActivo(true);
        usuarioService.save(usuario);
        return new ResponseEntity(new Mensaje("Usuario habilitado"), HttpStatus.OK);
    }
}