package com.upao.recicla.infra.repository;

import com.upao.recicla.domain.entity.Recompensa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecompensaRepository extends JpaRepository<Recompensa, Long> {
    Page<Recompensa> findAllByActivo(boolean activo, Pageable pageable);
    Optional<Recompensa> findByTitulo(String titulo);
}
