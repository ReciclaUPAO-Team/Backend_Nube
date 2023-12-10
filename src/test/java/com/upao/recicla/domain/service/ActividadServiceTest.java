package com.upao.recicla.domain.service;

import com.upao.recicla.domain.entity.Actividad;
import com.upao.recicla.domain.entity.NivelUsuario;
import com.upao.recicla.domain.entity.Residuo;
import com.upao.recicla.domain.entity.Usuario;
import com.upao.recicla.infra.email.QrCodeGenerator;
import com.upao.recicla.infra.repository.ActividadRepository;
import com.upao.recicla.infra.repository.ResiduoRepository;
import com.upao.recicla.infra.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class ActividadServiceTest {

    @Mock
    private ActividadRepository actividadRepository;

    @Mock
    private ResiduoRepository residuoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private ActividadService actividadService;

    @BeforeEach
    void setUp() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("nombreDeUsuario");

        // Configuración del mock de Usuario
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setNombre("Usuario Test");
        usuarioMock.setPuntos(100.0);
        usuarioMock.setNivel(NivelUsuario.PLATA);

        // Configuración del mock de Residuo
        Residuo residuoMock = new Residuo();
        residuoMock.setId(1L);
        residuoMock.setNombre("Papel");
        residuoMock.setPuntos(10.0);

        when(usuarioRepository.findByUsername("nombreDeUsuario")).thenReturn(Optional.of(usuarioMock));
        when(residuoRepository.findByNombre(anyString())).thenReturn(Optional.of(residuoMock));
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuarioMock));
    }

    @Test
    void getAllActividades() {
        // Creamos mocks de los repositorios necesarios
        ActividadRepository actividadRepositoryMock = Mockito.mock(ActividadRepository.class);
        ResiduoRepository residuoRepositoryMock = Mockito.mock(ResiduoRepository.class);
        UsuarioRepository usuarioRepositoryMock = Mockito.mock(UsuarioRepository.class);

        // Creamos una instancia del servicio de actividades con los mocks
        ActividadService actividadService = new ActividadService(
                actividadRepositoryMock,
                residuoRepositoryMock,
                usuarioRepositoryMock
        );

        // Simulamos la respuesta del repositorio de actividades
        Page<Actividad> actividades = Page.empty();
        when(actividadRepositoryMock.findAll(any(Pageable.class))).thenReturn(actividades);

        // Obtenemos todas las actividades
        Page<Actividad> actividadesObtenidas = actividadService.getAllActividades(PageRequest.of(0, 10));

        // Assert que la página de actividades obtenida sea la misma que la simulada
        assertEquals(actividades, actividadesObtenidas);
    }

    @Test
    void getActividadByIdTest() {
        Long id = 1L;
        Optional<Actividad> expectedActividad = Optional.of(new Actividad());

        when(actividadRepository.findById(id)).thenReturn(expectedActividad);

        Optional<Actividad> result = actividadService.getActividadById(id);

        assertEquals(expectedActividad, result);
        verify(actividadRepository).findById(id);
    }

    @Test
    void addActividadSuccessTest() {
        Actividad actividadTest = new Actividad();
        actividadTest.setCantidad(5.0);

        ResponseEntity<String> response = actividadService.addActividad(actividadTest, "Papel");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Actividad registrada con éxito"));
        verify(actividadRepository).save(any(Actividad.class));
    }

    @Test
    void getReferenceById() {
        // Creamos mocks de los repositorios necesarios
        ActividadRepository actividadRepositoryMock = Mockito.mock(ActividadRepository.class);
        ResiduoRepository residuoRepositoryMock = Mockito.mock(ResiduoRepository.class);
        UsuarioRepository usuarioRepositoryMock = Mockito.mock(UsuarioRepository.class);

        // Creamos una instancia del servicio de actividades con los mocks
        ActividadService actividadService = new ActividadService(
                actividadRepositoryMock,
                residuoRepositoryMock,
                usuarioRepositoryMock
        );

        // Simulamos la respuesta del repositorio
        Long actividadId = 1L;
        Actividad actividad = new Actividad();
        actividad.setId(actividadId);
        when(actividadRepositoryMock.getReferenceById(actividadId)).thenReturn(actividad);

        // Obtenemos la actividad por su ID
        Actividad actividadObtenida = actividadService.getReferenceById(actividadId);

        // Assert que la actividad obtenida sea la misma que la simulada
        assertEquals(actividad, actividadObtenida);
    }

    @Test
    void deleteActividadById() {
        // Creamos mocks de los repositorios necesarios
        ActividadRepository actividadRepositoryMock = Mockito.mock(ActividadRepository.class);
        ResiduoRepository residuoRepositoryMock = Mockito.mock(ResiduoRepository.class);
        UsuarioRepository usuarioRepositoryMock = Mockito.mock(UsuarioRepository.class);

        // Creamos una instancia del servicio de actividades con los mocks
        ActividadService actividadService = new ActividadService(
                actividadRepositoryMock,
                residuoRepositoryMock,
                usuarioRepositoryMock
        );

        // Simulamos la respuesta del repositorio
        Long actividadId = 1L;
        Actividad actividad = new Actividad();
        actividad.setId(actividadId);
        when(actividadRepositoryMock.getReferenceById(actividadId)).thenReturn(actividad);

        // Eliminamos la actividad por su ID
        actividadService.deleteActividadById(actividadId);

        // Assert que el método deleteById del repositorio se haya llamado con el ID de la actividad
        Mockito.verify(actividadRepositoryMock).deleteById(actividadId);
    }

}