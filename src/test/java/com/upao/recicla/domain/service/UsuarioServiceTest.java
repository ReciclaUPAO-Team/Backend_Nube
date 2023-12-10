package com.upao.recicla.domain.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.upao.recicla.domain.entity.Rol;
import com.upao.recicla.domain.entity.Usuario;
import com.upao.recicla.infra.repository.UsuarioRepository;
import com.upao.recicla.infra.security.JwtService;
import com.upao.recicla.infra.security.TokenResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    public void whenAddUsuario_thenSaveUsuario() {
        // Configuración
        Usuario usuario = new Usuario();
        usuario.setUsername("testUser");
        usuario.setPassword("testPass");

        when(passwordEncoder.encode(usuario.getPassword())).thenReturn("encodedPass");
        when(jwtService.getToken(any(Usuario.class), any(Usuario.class))).thenReturn("testToken");

        // Ejecución
        TokenResponse tokenResponse = usuarioService.addUsuario(usuario);

        // Verificación
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        assertEquals("testToken", tokenResponse.getToken());
    }

    @Test
    public void whenGetUsuarioById_thenReturnUsuario() {
        // Configuración
        Long id = 1L;
        Optional<Usuario> expectedUsuario = Optional.of(new Usuario());
        when(usuarioRepository.findById(id)).thenReturn(expectedUsuario);

        // Ejecución
        Optional<Usuario> result = usuarioService.getUsuarioById(id);

        // Verificación
        assertEquals(expectedUsuario, result);
    }

    @Test
    public void whenCambiarRolUsuario_thenChangeRol() {
        // Configuración
        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setRol(Rol.PARTICIPANTE);

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        // Ejecución
        usuarioService.cambiarRolUsuario(usuarioId, Rol.ADMINISTRADOR);

        // Verificación
        assertEquals(Rol.ADMINISTRADOR, usuario.getRol());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    public void whenUpdateUsuario_thenUpdateUsuario() {
        // Configuración
        Long id = 1L;
        Usuario existingUsuario = new Usuario();
        existingUsuario.setId(id);
        existingUsuario.setRol(Rol.PARTICIPANTE);

        Usuario updatedUsuario = new Usuario();
        updatedUsuario.setRol(Rol.ADMINISTRADOR);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(existingUsuario));

        // Ejecución
        usuarioService.updateUsuario(updatedUsuario, id);

        // Verificación
        verify(usuarioRepository, times(1)).save(existingUsuario);
        assertEquals(Rol.ADMINISTRADOR, existingUsuario.getRol());
    }

    @Test
    public void whenDeleteUsuarioById_thenDeleteUsuario() {
        // Configuración
        Long id = 1L;

        // Ejecución
        usuarioService.deleteUsuarioById(id);

        // Verificación
        verify(usuarioRepository, times(1)).deleteById(id);
    }

}