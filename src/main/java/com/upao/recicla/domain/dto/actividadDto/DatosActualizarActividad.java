package com.upao.recicla.domain.dto.actividadDto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Actividad}
 */
public record DatosActualizarActividad(@NotNull Long id, String nombre, Double cantidad, String imagen) implements Serializable {
}