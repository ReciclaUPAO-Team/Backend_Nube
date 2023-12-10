package com.upao.recicla.infra.repository;

import com.upao.recicla.domain.entity.Comunidad;
import com.upao.recicla.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComunidadRepository extends JpaRepository<Comunidad, Long> {
    Optional<Comunidad> findByNombreAndUsuariosContains(String nombre, Usuario usuario);
    boolean existsByNombre(String nombre);
}
