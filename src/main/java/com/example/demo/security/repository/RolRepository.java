package com.example.demo.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.security.entity.Rol;
import com.example.demo.security.enums.RolNombre;

public interface RolRepository extends JpaRepository<Rol, Integer> {
	Optional<Rol> findByRolNombre (RolNombre rolNombre);
}
