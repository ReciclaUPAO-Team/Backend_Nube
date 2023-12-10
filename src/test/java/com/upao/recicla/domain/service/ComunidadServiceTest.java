package com.upao.recicla.domain.service;

import com.upao.recicla.domain.entity.Comunidad;
import com.upao.recicla.domain.entity.Usuario;
import com.upao.recicla.infra.repository.ComunidadRepository;
import com.upao.recicla.infra.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComunidadServiceTest {

    @Mock
    private ComunidadRepository comunidadRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ComunidadService comunidadService;

    @Test
    public void whenAddComunidad_thenSaveComunidad() {
        // Configuración
        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Test Comunidad");

        Usuario usuario = new Usuario();
        usuario.setUsername("testUser");

        when(comunidadRepository.existsByNombre(anyString())).thenReturn(false);
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));

        // Simulando contexto de seguridad
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken("testUser", "password"));

        // Ejecución
        comunidadService.addComunidad(comunidad);

        // Verificación
        verify(comunidadRepository, times(1)).save(any(Comunidad.class));
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    public void whenAddExistingComunidad_thenThrowException() {
        // Configuración
        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Test Comunidad");

        when(comunidadRepository.existsByNombre(anyString())).thenReturn(true);

        // Ejecución y verificación
        assertThrows(RuntimeException.class, () -> comunidadService.addComunidad(comunidad));
    }

}