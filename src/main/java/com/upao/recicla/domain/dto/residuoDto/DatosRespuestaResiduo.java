package com.upao.recicla.domain.dto.residuoDto;

import java.io.Serializable;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Residuo}
 */
public record DatosRespuestaResiduo(Long id, String nombre, String descripcion, String tipo,
                                    Double puntos)  implements Serializable {
}