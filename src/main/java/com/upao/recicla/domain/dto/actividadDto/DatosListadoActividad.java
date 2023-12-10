package com.upao.recicla.domain.dto.actividadDto;

import com.upao.recicla.domain.entity.Actividad;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Actividad}
 */
public record DatosListadoActividad(Long id, String nombre, Double cantidad, String imagen,
                                    LocalDateTime fecha, Long residuo_id,  String nombre_residuo,
                                    String descripcion, String tipo, Double puntos_residuo,
                                    Long usuario_id, String nombre_usuario, Double puntos_usuario_totales) implements Serializable {
    public DatosListadoActividad(Actividad actividad) {
        this(actividad.getId(), actividad.getNombre(), actividad.getCantidad(), actividad.getImagen(),
                actividad.getFecha(), actividad.getId(), actividad.getResiduo().getNombre(), actividad.getResiduo().getDescripcion(),
                actividad.getResiduo().getTipo(), actividad.getResiduo().getPuntos(), actividad.getId(),
                actividad.getUsuario().getNombre(), actividad.getUsuario().getPuntos());
    }
}