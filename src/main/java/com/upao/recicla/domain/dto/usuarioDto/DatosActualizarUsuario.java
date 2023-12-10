package com.upao.recicla.domain.dto.usuarioDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Usuario}
 */
public record DatosActualizarUsuario(@NotNull Long id, String nombre, String edad,
                                     String telefono, @Email String correo) implements Serializable {
}