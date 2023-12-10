package com.upao.recicla.domain.service;

import com.upao.recicla.domain.dto.comunidadDto.DatosActualizarComunidad;
import com.upao.recicla.domain.entity.Comunidad;
import com.upao.recicla.domain.entity.Usuario;
import com.upao.recicla.infra.repository.ComunidadRepository;
import com.upao.recicla.infra.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ComunidadService {

    @Autowired
    private final ComunidadRepository comunidadRepository;
    private final UsuarioRepository usuarioRepository;

    public ComunidadService(ComunidadRepository comunidadRepository,
                            UsuarioRepository usuarioRepository) {
        this.comunidadRepository = comunidadRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void addComunidad(Comunidad comunidad) {
        if (comunidadRepository.existsByNombre(comunidad.getNombre())) {
            throw new RuntimeException("Ya existe una comunidad con este nombre.");
        }
        String nombreUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioCreador = usuarioRepository.findByUsername(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Usuario> usuariosDeLaComunidad = new ArrayList<>();
        usuariosDeLaComunidad.add(usuarioCreador);
        comunidad.setUsuarios(usuariosDeLaComunidad);

        List<Comunidad> comunidadesDelUsuario = new ArrayList<>(usuarioCreador.getComunidades());
        comunidadesDelUsuario.add(comunidad);
        usuarioCreador.setComunidades(comunidadesDelUsuario);

        comunidadRepository.save(comunidad);
        usuarioRepository.save(usuarioCreador);
    }

    public void unirseComunidad(Long idComunidad) {
        String nombreUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Comunidad comunidad = comunidadRepository.findById(idComunidad)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));

        if (comunidad.getUsuarios().contains(usuario)) {
            throw new RuntimeException("Ya eres miembro de esta comunidad.");
        }

        List<Usuario> usuariosDeLaComunidad = new ArrayList<>(comunidad.getUsuarios());
        usuariosDeLaComunidad.add(usuario);
        comunidad.setUsuarios(usuariosDeLaComunidad);

        List<Comunidad> comunidadesDelUsuario = new ArrayList<>(usuario.getComunidades());
        comunidadesDelUsuario.add(comunidad);
        usuario.setComunidades(comunidadesDelUsuario);

        if (!comunidad.getUsuarios().contains(usuario)) {
            comunidad.getUsuarios().add(usuario);
            comunidadRepository.save(comunidad);
        }
        usuarioRepository.save(usuario);
    }

    public Page<Comunidad> getAllComunidades(Pageable pageable) {
        return comunidadRepository.findAll(pageable);
    }

    public Comunidad getReferenceById(Long id) {
        return comunidadRepository.getReferenceById(id);
    }

    public void updateComunidad(String nombreComunidad, DatosActualizarComunidad datos) {
        String nombreUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioActual = usuarioRepository.findByUsername(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Comunidad comunidad = comunidadRepository.findByNombreAndUsuariosContains(nombreComunidad, usuarioActual)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada o no tienes permiso para actualizarla"));

        comunidad.setNombre(datos.nombre());
        comunidad.setDescripcion(datos.descripcion());
        comunidadRepository.save(comunidad);
    }

    public void deleteComunidad(String nombreComunidad) {
        String nombreUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioActual = usuarioRepository.findByUsername(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Comunidad comunidad = comunidadRepository.findByNombreAndUsuariosContains(nombreComunidad, usuarioActual)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada o no tienes permiso para eliminarla"));

        List<Usuario> usuarios = comunidad.getUsuarios();
        if (usuarios != null) {
            for (Usuario usuario : usuarios) {
                usuario.getComunidades().remove(comunidad);
                usuarioRepository.save(usuario);
            }
        }

        if (comunidad.getUsuarios().contains(usuarioActual)) {
            comunidadRepository.delete(comunidad);
        } else {
            throw new RuntimeException("No tienes permiso para eliminar esta comunidad");
        }
    }

}
