package com.upao.recicla.domain.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.upao.recicla.domain.entity.Residuo;
import com.upao.recicla.infra.repository.ResiduoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ResiduoServiceTest {

    @Mock
    private ResiduoRepository residuoRepository;

    @InjectMocks
    private ResiduoService residuoService;

    @Test
    public void whenGetAllResiduos_thenReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Residuo> expectedPage = new PageImpl<>(Arrays.asList(new Residuo(), new Residuo()));

        when(residuoRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Residuo> resultPage = residuoService.getAllResiduos(pageable);

        assertEquals(expectedPage, resultPage);
    }

    @Test
    public void whenGetResiduoById_thenReturnResiduo() {
        // Configuración
        Long id = 1L;
        Optional<Residuo> expectedResiduo = Optional.of(new Residuo());
        when(residuoRepository.findById(id)).thenReturn(expectedResiduo);

        // Ejecución
        Optional<Residuo> result = residuoService.getResiduoById(id);

        // Verificación
        assertEquals(expectedResiduo, result);
    }

    @Test
    public void whenAddResiduo_thenSaveResiduo() {
        // Configuración
        Residuo residuo = new Residuo();
        residuo.setNombre("Papel");

        when(residuoRepository.findByNombre(residuo.getNombre())).thenReturn(Optional.empty());

        // Ejecución
        residuoService.addResiduo(residuo);

        // Verificación
        verify(residuoRepository, times(1)).save(residuo);
    }

    @Test
    public void whenAddExistingResiduo_thenThrowException() {
        // Configuración
        Residuo residuo = new Residuo();
        residuo.setNombre("Papel");

        when(residuoRepository.findByNombre(residuo.getNombre())).thenReturn(Optional.of(residuo));

        // Ejecución y verificación
        assertThrows(IllegalArgumentException.class, () -> residuoService.addResiduo(residuo));
    }

    @Test
    public void whenUpdateResiduo_thenUpdateResiduo() {
        // Configuración
        Long id = 1L;
        Residuo existingResiduo = new Residuo();
        existingResiduo.setId(id);
        existingResiduo.setNombre("Papel");

        Residuo updatedResiduo = new Residuo();
        updatedResiduo.setNombre("Papel Reciclado");

        when(residuoRepository.findById(id)).thenReturn(Optional.of(existingResiduo));

        // Ejecución
        residuoService.updateResiduo(updatedResiduo, id);

        // Verificación
        verify(residuoRepository, times(1)).save(existingResiduo);
        assertEquals("Papel Reciclado", existingResiduo.getNombre());
    }


    @Test
    public void whenDeleteResiduoById_thenDeleteResiduo() {
        // Configuración
        Long id = 1L;

        // Ejecución
        residuoService.deleteResiduoById(id);

        // Verificación
        verify(residuoRepository, times(1)).deleteById(id);
    }

}