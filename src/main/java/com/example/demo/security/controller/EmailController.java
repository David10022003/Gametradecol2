package com.example.demo.security.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.security.dto.ChangePasswordDTO;
import com.example.demo.security.dto.ConfirmEmailDTO;
import com.example.demo.security.dto.EmailValuesDTO;
import com.example.demo.security.service.EmailService;
import com.example.demo.security.service.UsuarioService;
import com.example.demo.security.dto.Mensaje;
import com.example.demo.security.entity.Usuario;

@RestController
@RequestMapping("/email")
@CrossOrigin
public class EmailController {
    @Autowired
    EmailService emailService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @PostMapping("/send-email-password")
    public ResponseEntity<?> sendEmailPassword(@RequestBody EmailValuesDTO dto) {
        Optional<Usuario> usuarioOpt = usuarioService.getByEmail(dto.getMailTo());
        if (!usuarioOpt.isPresent())
            return new ResponseEntity(new Mensaje("No existe ningun usuario con ese correo"), HttpStatus.NOT_FOUND);
        Usuario usuario = usuarioOpt.get();
        dto.setMailFrom(mailFrom);
        dto.setMailTo(usuario.getEmail());
        dto.setSubject("Cambio de contraseña");
        dto.setUserName(usuario.getUsername());
        UUID uuid = UUID.randomUUID();
        String tokenPassword = uuid.toString();
        dto.setTokenPassword(tokenPassword);
        usuario.setTokenPassword(tokenPassword);
        usuarioService.save(usuario);
        emailService.sendEmailChange(dto);
        return new ResponseEntity(new Mensaje("Te hemos enviado un correo"), HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos mal diligenciados"), HttpStatus.BAD_REQUEST);
        if (!dto.getPassword().equals(dto.getConfirmPassword()))
            return new ResponseEntity(new Mensaje("Las contraseñas no coinciden"), HttpStatus.BAD_REQUEST);
        Optional<Usuario> usuarioOpt = usuarioService.getByTokenPassword(dto.getTokenPassword());
        if (!usuarioOpt.isPresent())
            return new ResponseEntity(new Mensaje("No existe ningun usuario con esas credenciales"),
                    HttpStatus.NOT_FOUND);
        Usuario usuario = usuarioOpt.get();
        String newPassword = passwordEncoder.encode(dto.getPassword());
        usuario.setPassword(newPassword);
        usuario.setTokenPassword(null);
        usuarioService.save(usuario);
        return new ResponseEntity(new Mensaje("Contraseña actualizada"), HttpStatus.OK);
    }

    @PostMapping("/send-email-confirm")
    public ResponseEntity<?> sendEmailConfirm(@RequestBody EmailValuesDTO dto) {
        Optional<Usuario> usuarioOpt = usuarioService.getByEmail(dto.getMailTo());
        if (!usuarioOpt.isPresent())
            return new ResponseEntity(new Mensaje("No existe ningun usuario con ese correo"), HttpStatus.NOT_FOUND);
        Usuario usuario = usuarioOpt.get();
        dto.setMailFrom(mailFrom);
        dto.setMailTo(usuario.getEmail());
        dto.setSubject("Confirmación de correo");
        dto.setUserName(usuario.getUsername());
        emailService.sendEmailConfirm(dto);
        return new ResponseEntity(new Mensaje("Te hemos enviado un correo"), HttpStatus.OK);
    }

    @PostMapping("/confirmar-email")
    public ResponseEntity<?> confirmarEmail(@RequestBody ConfirmEmailDTO dto) {
        Optional<Usuario> usuarioOpt = usuarioService.getByUsername(dto.getUsername());
        if (!usuarioOpt.isPresent())
            return new ResponseEntity(new Mensaje("No existe ningun usuario con esas credenciales"),
                    HttpStatus.NOT_FOUND);
        dto.setConfirmacion(true);
        Usuario usuario = usuarioOpt.get();
        usuario.setCorreoConfirmado(dto.getConfirmacion());
        usuarioService.save(usuario);
        return new ResponseEntity(new Mensaje("Correo confirmado"), HttpStatus.OK);
    }
}