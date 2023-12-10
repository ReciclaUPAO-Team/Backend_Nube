package com.upao.recicla.domain.dto.residuoDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Residuo}
 */
public record DatosRegistroResiduo(@NotBlank String nombre, String descripcion, @NotBlank String tipo, @NotNull Double puntos)
                implements Serializable {
}