package com.upao.recicla.domain.dto.actividadDto;

import java.io.Serializable;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Actividad}
 */
public record DatosRegistroActividad(Long id, String nombre, Double cantidad, String imagen,
                                     Long residuo_id, Long usuario_id) implements Serializable {
}