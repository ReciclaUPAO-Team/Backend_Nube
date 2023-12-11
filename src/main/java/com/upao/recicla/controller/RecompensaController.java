package com.upao.recicla.controller;

import com.upao.recicla.domain.dto.recompensaDto.*;
import com.upao.recicla.domain.entity.Recompensa;
import com.upao.recicla.domain.service.RecompensaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/recompensa")
@RequiredArgsConstructor
public class RecompensaController {

    @Autowired
    private final RecompensaService recompensaService;

    private final String uploadDirectory =
            System.getProperty("user.dir") + "/src/main/resources/static/recompensas";

    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
    @PostMapping("/crear")
    @Transactional
    public ResponseEntity<?> addRecompensas(@RequestBody @Valid DatosRegistroRecompensa datosRegistroRecompensa,
                                         UriComponentsBuilder uriBuilder) throws IOException {

        String base64Image = datosRegistroRecompensa.imagenPath();

        if (base64Image == null || base64Image.trim().isEmpty()) {
            throw new IllegalArgumentException("La imagen no puede ser nula o vacía.");
        }

        byte[] imageBytes;
        try {
            imageBytes = Base64.getDecoder().decode(base64Image);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Formato de imagen no válido.", e);
        }

        Path directoryPath = Paths.get(uploadDirectory);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        String originalFilename = UUID.randomUUID().toString();
        Path fileNameAndPath = Paths.get(uploadDirectory, originalFilename);
        Files.write(fileNameAndPath, imageBytes);

        Recompensa nuevaRecompensa = new Recompensa();
        nuevaRecompensa.setTitulo(datosRegistroRecompensa.titulo());
        nuevaRecompensa.setDescripcion(datosRegistroRecompensa.descripcion());
        nuevaRecompensa.setCategoria(datosRegistroRecompensa.categoria());
        nuevaRecompensa.setValor(datosRegistroRecompensa.valor());
        nuevaRecompensa.setImagenPath(base64Image);
        recompensaService.addRecompensa(nuevaRecompensa);

        var uri = uriBuilder.path("/recompensa/{id}").buildAndExpand(nuevaRecompensa.getId()).toUri();
        return ResponseEntity.created(uri).body(new DatosDetallesRecompensa(nuevaRecompensa));
    }

    @GetMapping("/catalogo")
    public ResponseEntity<Page<DatosListadoRecompensa>> listarRecompensas(@PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(recompensaService.getAllRecompensas(pageable).map(DatosListadoRecompensa::new));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaRecompensa> getRecompensaById(@PathVariable Long id) {
        Recompensa recompensa = recompensaService.getReferenceById(id);
        var datosRecompensa = new DatosRespuestaRecompensa(recompensa.getId(), recompensa.getTitulo(), recompensa.getDescripcion(), recompensa.getCategoria(),
                recompensa.getValor(), recompensa.getFechaInicio(), recompensa.getFechaCierre());
        return ResponseEntity.ok(datosRecompensa);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
    @PutMapping("/editar/{id}")
    @Transactional
    public ResponseEntity updateRecompensa(@RequestBody @Validated DatosActualizarRecompensa datos) {
        var recompensa = recompensaService.getReferenceById(datos.id());
        recompensa.actualizarRecompensa(datos);

        return ResponseEntity.ok(new DatosDetallesRecompensa(recompensa));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteRecompensaById(@PathVariable Long id) {
        var recompensa = recompensaService.getReferenceById(id);
        recompensaService.deleteRecompensaById(id);
        return ResponseEntity.noContent().build();
    }

}
