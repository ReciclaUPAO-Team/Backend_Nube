package com.upao.recicla.domain.dto.usuarioDto;

import com.upao.recicla.domain.entity.Rol;
import com.upao.recicla.domain.entity.Usuario;
import jakarta.validation.constraints.Email;

import java.io.Serializable;

/**
 * DTO for {@link com.upao.recicla.domain.entity.Usuario}
 */
public record DatosListadoUsuario(Long id, String nombre, String edad, String telefono, @Email String correo,
                                  Double puntos) implements Serializable {
    public DatosListadoUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getNombre(), usuario.getEdad(), usuario.getTelefono(), usuario.getCorreo(),
                usuario.getPuntos());
    }
}