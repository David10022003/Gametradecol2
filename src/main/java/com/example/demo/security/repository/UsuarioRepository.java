package com.example.demo.security.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.security.entity.Usuario;

@Repository
@Qualifier("usuario")
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    boolean existsById(String id);

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByTokenPassword(String tokenPassword);

    List<Usuario> findAll();

    @Query("SELECT u.username FROM Usuario u WHERE u.activo = true")
    List<String> findUsernameByActivoIsTrue();
}