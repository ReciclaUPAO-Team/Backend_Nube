package com.upao.recicla.domain.dto.usuarioDto;

import com.upao.recicla.domain.entity.Rol;
import com.upao.recicla.domain.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Usuario}
 */
public record DatosDetallesUsuario(Long id, String nombre, String edad, String telefono, @Email String correo,
                                   Double puntos, @Pattern(regexp = "\\d{8}") String dni,
                                   Rol rol) implements Serializable {
    public DatosDetallesUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getNombre(), usuario.getEdad(), usuario.getTelefono(), usuario.getCorreo(),
                usuario.getPuntos(), usuario.getDni(), usuario.getRol());
    }
}