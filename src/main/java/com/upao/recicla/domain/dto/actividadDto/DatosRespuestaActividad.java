package com.upao.recicla.domain.dto.actividadDto;

import com.upao.recicla.domain.entity.Actividad;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Actividad}
 */
public record DatosRespuestaActividad(Long id, String nombre, Double cantidad, String imagen,
                                      LocalDateTime fecha, Double puntosGanados, Long residuo_id, String residuoNombre,
                                      String residuoDescripcion, String residuoTipo, Double residuoPuntos,
                                      Long usuario_id, String usuarioNombre, Double usuarioPuntos) implements Serializable {
    public DatosRespuestaActividad(Actividad actividad){
        this(actividad.getId(), actividad.getNombre(), actividad.getCantidad(), actividad.getImagen(),
                actividad.getFecha(), actividad.getCantidad() * actividad.getResiduo().getPuntos(),
                actividad.getResiduo().getId(), actividad.getResiduo().getNombre(),
                actividad.getResiduo().getDescripcion(), actividad.getResiduo().getTipo(), actividad.getResiduo().getPuntos(),
                actividad.getUsuario().getId(), actividad.getUsuario().getNombre(), actividad.getUsuario().getPuntos()
                );
    }
}