package com.upao.recicla.domain.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.upao.recicla.domain.entity.Recompensa;
import com.upao.recicla.infra.repository.RecompensaRepository;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class RecompensaServiceTest {

    @Mock
    private RecompensaRepository recompensaRepository;

    @InjectMocks
    private RecompensaService recompensaService;

    @Test
    void addRecompensa() {
        // Creamos un mock de la clase RecompensaRepository
        RecompensaRepository recompensaRepositoryMock = Mockito.mock(RecompensaRepository.class);

        // Creamos una instancia del servicio de recompensas
        RecompensaService recompensaService = new RecompensaService(recompensaRepositoryMock);

        // Creamos una recompensa
        Recompensa recompensa = new Recompensa();
        recompensa.setTitulo("Título de la recompensa");
        recompensa.setDescripcion("Descripción de la recompensa");
        recompensa.setCategoria("Categoría de la recompensa");
        recompensa.setValor(100.0);

        // Simulamos el comportamiento del repositorio
        Mockito.when(recompensaRepositoryMock.save(recompensa)).thenReturn(recompensa);

        // Agregamos la recompensa
        Recompensa recompensaAgregada = recompensaService.addRecompensa(recompensa);

        // Assert que la recompensa se haya agregado correctamente
        assertEquals(recompensa, recompensaAgregada);

        // Assert que el repositorio haya sido llamado para guardar la recompensa
        Mockito.verify(recompensaRepositoryMock).save(recompensa);
    }

    @Test
    void getAllRecompensas() {
        // Creamos un mock de la clase RecompensaRepository
        RecompensaRepository recompensaRepositoryMock = Mockito.mock(RecompensaRepository.class);

        // Creamos una instancia del servicio de recompensas
        RecompensaService recompensaService = new RecompensaService(recompensaRepositoryMock);

        // Simulamos la respuesta de la base de datos
        List<Recompensa> recompensas = Arrays.asList(
                new Recompensa(),
                new Recompensa()
        );
        Page<Recompensa> recompensasPage = new PageImpl<>(recompensas);

        Mockito.when(recompensaRepositoryMock.findAll(any(Pageable.class))).thenReturn(recompensasPage);

        // Obtenemos todas las recompensas
        Page<Recompensa> recompensasObtenidas = recompensaService.getAllRecompensas(PageRequest.of(0, 10));
        assertNotNull(recompensasObtenidas);

        // Extraemos la lista de recompensas del objeto Page
        List<Recompensa> recompensasObtenidasList = recompensasObtenidas.getContent();

        // Assert que las recompensas obtenidas sean las mismas que las simuladas
        assertEquals(recompensas, recompensasObtenidasList);
    }

    @Test
    void getReferenceById() {
        // Creamos un mock de la clase RecompensaRepository
        RecompensaRepository recompensaRepositoryMock = Mockito.mock(RecompensaRepository.class);

        // Creamos una instancia del servicio de recompensas
        RecompensaService recompensaService = new RecompensaService(recompensaRepositoryMock);

        // Simulamos la respuesta del repositorio
        Long recompensaId = 1L;
        Recompensa recompensa = new Recompensa();
        recompensa.setId(recompensaId);
        Mockito.when(recompensaRepositoryMock.getReferenceById(recompensaId)).thenReturn(recompensa);

        // Obtenemos la recompensa por su ID
        Recompensa recompensaObtenida = recompensaService.getReferenceById(recompensaId);

        // Assert que la recompensa obtenida sea la misma que la simulada
        assertNotNull(recompensaObtenida);
        assertEquals(recompensa, recompensaObtenida);

        // Assert que el repositorio haya sido llamado para obtener la recompensa por su ID
        Mockito.verify(recompensaRepositoryMock).getReferenceById(recompensaId);
    }

    @Test
    void deleteRecompensaById() {
        // Creamos un mock de la clase RecompensaRepository
        RecompensaRepository recompensaRepositoryMock = Mockito.mock(RecompensaRepository.class);

        // Creamos una instancia del servicio de recompensas
        RecompensaService recompensaService = new RecompensaService(recompensaRepositoryMock);

        // Simulamos la respuesta del repositorio
        Long recompensaId = 1L;
        Mockito.doNothing().when(recompensaRepositoryMock).deleteById(recompensaId);

        // Eliminamos la recompensa por su ID
        recompensaService.deleteRecompensaById(recompensaId);

        // Assert que el repositorio haya sido llamado para eliminar la recompensa por su ID
        Mockito.verify(recompensaRepositoryMock).deleteById(recompensaId);
    }

    @Test
    public void whenAddInvalidRecompensa_thenThrowException() {
        // Configuración
        Recompensa recompensa = new Recompensa();
        recompensa.setValor(0.0);

        // Ejecución y verificación
        assertThrows(IllegalArgumentException.class, () -> recompensaService.addRecompensa(recompensa));
    }

    @Test
    public void whenEliminarRecompensasExpiradas_thenCallDelete() {
        // Configuración
        Recompensa recompensaExpirada = new Recompensa();
        recompensaExpirada.setFechaCierre(LocalDateTime.now().minusDays(1)); // Fecha en el pasado
        List<Recompensa> recompensasExpiradas = Arrays.asList(recompensaExpirada);

        when(recompensaRepository.findAll()).thenReturn(recompensasExpiradas);

        // Ejecución
        recompensaService.eliminarRecompensasExpiradas();

        // Verificación
        verify(recompensaRepository, times(1)).deleteAll(recompensasExpiradas);
    }



}