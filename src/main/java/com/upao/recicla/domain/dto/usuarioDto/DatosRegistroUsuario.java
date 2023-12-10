package com.upao.recicla.domain.dto.usuarioDto;

import com.upao.recicla.domain.entity.Rol;
import jakarta.validation.constraints.*;

import java.io.Serializable;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Usuario}
 */
public record DatosRegistroUsuario(@NotBlank String nombre, String edad, @Size(min = 0, max = 9) String telefono,
                                   @Email @NotBlank String correo, @Pattern(regexp = "\\d{8}") String dni,
                                   @NotBlank String username, @NotBlank String password) implements Serializable {
}