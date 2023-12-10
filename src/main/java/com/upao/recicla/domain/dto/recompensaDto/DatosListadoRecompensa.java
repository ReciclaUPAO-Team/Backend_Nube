package com.upao.recicla.domain.dto.recompensaDto;

import com.upao.recicla.domain.entity.Recompensa;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Recompensa}
 */
public record DatosListadoRecompensa(Long id, String titulo, String descripcion, String categoria, Double valor,
                                     String imagenPath, LocalDateTime fechaInicio, LocalDateTime fechaCierre) implements Serializable {
    public DatosListadoRecompensa(Recompensa recompensa){
        this(recompensa.getId(), recompensa.getTitulo(), recompensa.getDescripcion(), recompensa.getCategoria(),
                recompensa.getValor(), recompensa.getImagenPath(), recompensa.getFechaInicio(), recompensa.getFechaCierre());
    }
}