package com.upao.recicla.domain.service;

import com.upao.recicla.domain.entity.Recompensa;
import com.upao.recicla.domain.entity.Usuario;
import com.upao.recicla.infra.repository.RecompensaRepository;
import com.upao.recicla.infra.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CanjeServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private RecompensaRepository recompensaRepository;

    @InjectMocks
    private CanjeService canjeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("nombreDeUsuario");
    }

    @Test
    void canjearPuntosFallaPorPuntosInsuficientes() {
        Usuario usuarioMock = new Usuario();
        usuarioMock.setNombre("Usuario Test");
        usuarioMock.setPuntos(100.0);
        usuarioMock.setCorreo("usuario@test.com");

        Recompensa recompensaMock = new Recompensa();
        recompensaMock.setTitulo("Recompensa Test");
        recompensaMock.setValor(150.0);

        when(usuarioRepository.findByUsername("nombreDeUsuario")).thenReturn(Optional.of(usuarioMock));
        when(recompensaRepository.findByTitulo("Recompensa Test")).thenReturn(Optional.of(recompensaMock));

        Exception exception = assertThrows(Exception.class, () -> {
            canjeService.canjearPuntos("Recompensa Test");
        });

        assertTrue(exception.getMessage().contains("No tienes suficientes puntos para canjear esta recompensa"));
    }
}