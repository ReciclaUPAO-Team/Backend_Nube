package com.upao.recicla.domain.dto.recompensaDto;

import com.upao.recicla.domain.entity.Recompensa;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Recompensa}
 */
public record DatosDetallesRecompensa(Long id, String titulo, String descripcion, String categoria, Double valor)
        implements Serializable {
    public DatosDetallesRecompensa(Recompensa recompensa) {
        this(recompensa.getId(), recompensa.getTitulo(), recompensa.getDescripcion(), recompensa.getCategoria(),
                recompensa.getValor());
    }
}