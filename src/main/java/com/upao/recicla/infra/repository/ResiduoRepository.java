package com.upao.recicla.infra.repository;

import com.upao.recicla.domain.entity.Residuo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ResiduoRepository extends JpaRepository<Residuo, Long> {
    Optional<Residuo> findByNombre(String nombre);
}
