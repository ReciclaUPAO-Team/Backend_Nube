package com.upao.recicla.domain.dto.usuarioDto;

import com.upao.recicla.domain.entity.Rol;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Usuario}
 */
public record DatosActualizarRolUsuario(@NotNull Long id, Rol rol) implements Serializable {
}