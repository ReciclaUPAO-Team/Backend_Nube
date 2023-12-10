package com.upao.recicla.domain.dto.actividadDto;

import com.upao.recicla.domain.entity.Actividad;

import java.io.Serializable;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Actividad}
 */
public record DatosDetallesActividad(Long id, String nombre, Double cantidad, String imagen,
                                     Long residuo_id, Long usuario_id) implements Serializable {
    public DatosDetallesActividad(Actividad actividad){
        this(actividad.getId(), actividad.getNombre(), actividad.getCantidad(), actividad.getImagen(),
             actividad.getResiduo().getId(), actividad.getUsuario().getId());
    }
}