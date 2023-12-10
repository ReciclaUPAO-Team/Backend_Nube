package com.upao.recicla.domain.dto.residuoDto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Residuo}
 */
public record DatosActualizarResiduo(@NotNull Long id, String nombre, String descripcion, String tipo)
    implements Serializable {
}