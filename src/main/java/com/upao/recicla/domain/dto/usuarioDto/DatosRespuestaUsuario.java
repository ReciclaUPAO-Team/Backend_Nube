package com.upao.recicla.domain.dto.usuarioDto;

import com.upao.recicla.domain.entity.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Usuario}
 */
public record DatosRespuestaUsuario(Long id, String nombre, String edad, String telefono, @Email String correo,
                                    Double puntos, @Pattern(regexp = "\\d{8}") String dni,
                                    Rol rol) implements Serializable {
}