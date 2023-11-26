package com.example.demo.security.entity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.security.entity.Usuario;

public class UsuarioPrincipal implements UserDetails {
	private int id;
	private String nombre;
	private String apellido;
	private String email;
	private String username;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;

	public UsuarioPrincipal(int id, String nombre, String apellido, String email, String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}

	public static UsuarioPrincipal build(Usuario usuario) {
		List<GrantedAuthority> authorities = usuario.getRoles().stream().map(rol -> new SimpleGrantedAuthority(rol
				.getRolNombre().name())).collect(Collectors.toList());
		return new UsuarioPrincipal(usuario.getId(), usuario.getNombre(), usuario.getApellido(), usuario.getEmail(),
				usuario.getUsername(), usuario.getPassword(), authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public int getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

}
