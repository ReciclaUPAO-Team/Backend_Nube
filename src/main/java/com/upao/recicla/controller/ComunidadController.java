package com.upao.recicla.controller;

import com.upao.recicla.domain.dto.comunidadDto.*;
import com.upao.recicla.domain.entity.Comunidad;
import com.upao.recicla.domain.service.ComunidadService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/comunidad")
@RequiredArgsConstructor
public class ComunidadController {

    @Autowired
    private final ComunidadService comunidadService;

    @PostMapping("/nuevo")
    @Transactional
    public ResponseEntity<?> addComunidad(@RequestBody @Valid DatosRegistroComunidad datos,
                                       UriComponentsBuilder uriBuilder) {

        Comunidad comunidad = new Comunidad(datos);
        comunidadService.addComunidad(comunidad);

        URI uri = uriBuilder.path("/comunidad/{id}").buildAndExpand(comunidad.getId()).toUri();
        return ResponseEntity.created(uri).body(new DatosDetallesComunidad(comunidad));
    }

    @GetMapping("/mostrar")
    public ResponseEntity<Page<DatosListadoComunidad>> getAllComunidades(@PageableDefault (size = 10) Pageable pageable) {

        return ResponseEntity.ok(comunidadService.getAllComunidades(pageable).map(DatosListadoComunidad::new));
    }

    @PostMapping("/unirse/{idComunidad}")
    @Transactional
    public ResponseEntity<?> unirseComunidad(@PathVariable Long idComunidad) {
        try {
            comunidadService.unirseComunidad(idComunidad);
            return ResponseEntity.ok().body("Te has unido a la comunidad con éxito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/actualizar/{nombreComunidad}")
    @Transactional
    public ResponseEntity<?> updateComunidad(@PathVariable String nombreComunidad,
                                             @RequestBody DatosActualizarComunidad datos) {
        try {
            comunidadService.updateComunidad(nombreComunidad, datos);
            return ResponseEntity.ok().body("Comunidad actualizada con éxito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/eliminar/{nombreComunidad}")
    @Transactional
    public ResponseEntity<?> deleteComunidad(@PathVariable String nombreComunidad) {
        try {
            comunidadService.deleteComunidad(nombreComunidad);
            return ResponseEntity.ok().body("Comunidad eliminada con éxito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
