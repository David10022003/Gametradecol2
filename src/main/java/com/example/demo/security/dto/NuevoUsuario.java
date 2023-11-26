package com.example.demo.security.dto;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class NuevoUsuario {

	@NotBlank(message = "numero de identificacion obligatorio")
	private int id;
	@NotBlank(message = "nombre obligatorio")
	private String nombre;
	@NotBlank(message = "apellido obligatorio")
	private String apellido;
	@Email(message = "email no valido")
	@NotBlank(message = "email obligatorio")
	private String email;
	@NotBlank(message = "username obligatorio")
	private String username;
	@NotBlank(message = "contraseña obligatoria")
	private String password;
	@NotBlank(message = "confirmacion de contraseña obligatoria")
	private String confirmPassword;
	@NotBlank(message = "direccion obligatoria")
	private String direccion;

	private String captchaVal;
	private String rol;
	private Set<String> roles = new HashSet<>();

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getCaptchaVal() {
		return captchaVal;
	}

	public void setCaptchaVal(String captchaVal) {
		this.captchaVal = captchaVal;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

}
