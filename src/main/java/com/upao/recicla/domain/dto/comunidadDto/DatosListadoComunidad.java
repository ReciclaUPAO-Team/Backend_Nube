package com.upao.recicla.domain.dto.comunidadDto;

import com.upao.recicla.domain.entity.Comunidad;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Comunidad}
 */
public record DatosListadoComunidad(Long id, String fechaCreacion, String nombre, String descripcion,
                                    List<UsuarioDto> usuarios) implements Serializable {
    public DatosListadoComunidad(Comunidad comunidad) {
        this(comunidad.getId(), comunidad.getFechaCreacion(), comunidad.getNombre(), comunidad.getDescripcion(),
                comunidad.getUsuarios().stream().map(usuario -> new UsuarioDto(usuario.getId(), usuario.getNombre(),
                        usuario.getPuntos(), usuario.getComunidades().stream()
                                .map(comunidad1 -> new UsuarioDto.ComunidadDto(comunidad1.getId(),
                                        comunidad1.getNombre())).toList())).toList());
    }
    /**
     * DTO for {@link com.upao.recicla.domain.entity.Usuario}
     */
    public record UsuarioDto(Long id, String nombre, Double puntos,
                             List<ComunidadDto> comunidades) implements Serializable {
        /**
         * DTO for {@link com.upao.recicla.domain.entity.Comunidad}
         */
        public record ComunidadDto(Long id, String nombre) implements Serializable {
        }
    }
}