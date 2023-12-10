package com.upao.recicla.domain.dto.comunidadDto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Comunidad}
 */
public record DatosRespuestaComunidad(Long id, String fechaCreacion, String nombre, String descripcion,
                                      List<UsuarioDto> usuarios) implements Serializable {
    /**
     * DTO for {@link com.upao.recicla.domain.entity.Usuario}
     */
    public record UsuarioDto(Long id, String nombre) implements Serializable {
    }
}