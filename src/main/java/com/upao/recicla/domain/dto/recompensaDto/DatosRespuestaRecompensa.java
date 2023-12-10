package com.upao.recicla.domain.dto.recompensaDto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Recompensa}
 */
public record DatosRespuestaRecompensa(Long id, String titulo, String descripcion, String categoria, Double valor,
                                       LocalDateTime fechaInicio, LocalDateTime fechaCierre) implements Serializable {
}