package com.example.demo.security.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Usuario {

    @Id
    @Column(unique = true)
    @NotNull
    private int id;

    @NotNull
    private String nombre;

    @NotNull
    private String apellido;

    @NotNull
    private String direccion;

    @Column(unique = true)
    @NotNull
    private String email;

    @Column(unique = true)
    @NotNull
    private String username;

    @NotNull
    private String password;

    private String tokenPassword;

    @NotNull
    private Boolean correoConfirmado;

    @NotNull
    private Boolean activo;

    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_rol", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> roles = new HashSet<>();

    public Usuario(int id, @NotNull String nombre, @NotNull String apellido, @NotNull String email,
            @NotNull String username, @NotNull String password, @NotNull Boolean correoConfirmado,
            @NotNull String direccion, @NotNull Boolean activo) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.username = username;
        this.password = password;
        this.correoConfirmado = correoConfirmado;
        this.direccion = direccion;
        this.activo = activo;
    }

    public Usuario() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTokenPassword() {
        return tokenPassword;
    }

    public void setTokenPassword(String tokenPassword) {
        this.tokenPassword = tokenPassword;
    }

    public Boolean getCorreoConfirmado() {
        return correoConfirmado;
    }

    public void setCorreoConfirmado(Boolean correoConfirmado) {
        this.correoConfirmado = correoConfirmado;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

}