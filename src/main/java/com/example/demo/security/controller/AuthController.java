package com.example.demo.security.controller;

import java.io.IOException;
import java.lang.Boolean;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.security.entity.Usuario;
import com.example.demo.security.dto.JwtDto;
import com.example.demo.security.dto.LoginUsuario;
import com.example.demo.security.dto.Mensaje;
import com.example.demo.security.dto.NuevoUsuario;
import com.example.demo.security.entity.Rol;
import com.example.demo.security.enums.RolNombre;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.security.service.RolService;
import com.example.demo.security.service.UsuarioService;

import jakarta.mail.internet.ParseException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Value("${hCaptcha.secret.key")
    private String hCaptchaSecretKey;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolService rolService;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult)
            throws IOException, InterruptedException {
        if (bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("campos mal puestos o email inválido"), HttpStatus.BAD_REQUEST);
        if (usuarioService.existsById(nuevoUsuario.getId()))
            return new ResponseEntity(new Mensaje("ese numero de identificacion ya esta registrado"),
                    HttpStatus.BAD_REQUEST);
        if (usuarioService.existsByEmail(nuevoUsuario.getEmail()))
            return new ResponseEntity(new Mensaje("ese email ya existe"), HttpStatus.BAD_REQUEST);
        if (usuarioService.existsByUsername(nuevoUsuario.getUsername()))
            return new ResponseEntity(new Mensaje("ese nombre de usuario ya existe"), HttpStatus.BAD_REQUEST);
        if (!nuevoUsuario.getPassword().equals(nuevoUsuario.getConfirmPassword()))
            return new ResponseEntity(new Mensaje("Las contraseñas no coinciden"), HttpStatus.BAD_REQUEST);

        if (!StringUtils.hasText(nuevoUsuario.getCaptchaVal()))
            return new ResponseEntity(new Mensaje("error en el captcha"),
                    HttpStatus.BAD_REQUEST);
        HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        StringBuilder sb = new StringBuilder();
        sb.append("response=");
        sb.append(nuevoUsuario.getCaptchaVal());
        sb.append("&secret=");
        sb.append(this.hCaptchaSecretKey);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://hcaptcha.com/siteverify"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .timeout(Duration.ofSeconds(10)).POST(BodyPublishers.ofString(sb.toString()))
                .build();
        HttpResponse<String> response = httpClient.send(request,
                BodyHandlers.ofString());
        String[] arr = response.body().split("\\,");
        String value = arr[0].substring(arr[0].indexOf(":") + 1, arr[0].length());
        Boolean success = Boolean.valueOf(value);
        if (!success)
            return new ResponseEntity(new Mensaje("Realice nuevamente el captcha"),
                    HttpStatus.BAD_REQUEST);

        //

        Usuario usuario = new Usuario(nuevoUsuario.getId(), nuevoUsuario.getNombre(), nuevoUsuario.getApellido(),
                nuevoUsuario.getEmail(), nuevoUsuario.getUsername(),
                passwordEncoder.encode(nuevoUsuario.getPassword()), false, nuevoUsuario.getDireccion(), true);
        Set<Rol> roles = new HashSet<>();
        if (nuevoUsuario.getRol().equals("user")) {
            roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
            roles.add(rolService.getByRolNombre(RolNombre.ROLE_SELLER).get());
        } else if (nuevoUsuario.getRol().equals("admin"))
            roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
        usuario.setRoles(roles);
        usuarioService.save(usuario);
        return new ResponseEntity(new Mensaje("Usuario guardado"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos mal puestos"), HttpStatus.BAD_REQUEST);
        Optional<Usuario> usuarioOpt = usuarioService.getByUsername(loginUsuario.getUsername());
        if (usuarioOpt.isPresent()) {
            Boolean correoConfirmado = usuarioOpt.get().getCorreoConfirmado();
            if (!correoConfirmado)
                return new ResponseEntity(new Mensaje("No ha verificado su correo"), HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUsuario.getUsername(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println(userDetails.getAuthorities());
        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity(jwtDto, HttpStatus.OK);
    }

}