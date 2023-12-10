package com.upao.recicla.domain.dto.recompensaDto;

import java.io.Serializable;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Recompensa}
 */
public record DatosRegistroRecompensa(String titulo, String descripcion, String categoria,
                                      Double valor, String imagenPath) implements Serializable {
}