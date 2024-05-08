package com.upao.recicla.controller;

import com.upao.recicla.domain.dto.recompensaDto.*;
import com.upao.recicla.domain.entity.Recompensa;
import com.upao.recicla.domain.service.RecompensaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/recompensa")
@RequiredArgsConstructor
public class RecompensaController {

    @Autowired
    private final RecompensaService recompensaService;

    private static final List<String> FORMATOS_PERMITIDOS = Arrays.asList("image/png", "image/jpeg", "image/jpg");
    private static final long TAMANO_MAXIMO = 5 * 1024 * 1024; // 5MB

    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
    @PostMapping("/crear")
    @Transactional
    public ResponseEntity<?> addRecompensas(@RequestParam("titulo") String titulo,
                                            @RequestParam("descripcion") String descripcion,
                                            @RequestParam("categoria") String categoria,
                                            @RequestParam("valor") Double valor,
                                            @RequestParam("imagenPath") MultipartFile imagen) throws IOException {

        String imagenPath = guardarImagen(imagen);

        DatosRegistroRecompensa datosRegistroRecompensa = new DatosRegistroRecompensa(titulo, descripcion, categoria, valor, imagenPath);

        Recompensa nuevaRecompensa = new Recompensa();
        nuevaRecompensa.setTitulo(datosRegistroRecompensa.titulo());
        nuevaRecompensa.setDescripcion(datosRegistroRecompensa.descripcion());
        nuevaRecompensa.setCategoria(datosRegistroRecompensa.categoria());
        nuevaRecompensa.setValor(datosRegistroRecompensa.valor());
        if (imagen != null) {
            nuevaRecompensa.setImagenPath(guardarImagen(imagen));
        }

        recompensaService.addRecompensa(nuevaRecompensa);
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.path("/recompensa/{id}").buildAndExpand(nuevaRecompensa.getId()).toUri();
        return ResponseEntity.created(builder.build().toUri()).body(new DatosDetallesRecompensa(nuevaRecompensa));
    }

    private String guardarImagen(MultipartFile imagen) throws IOException {
        if (!imagen.isEmpty()) {
            if (!FORMATOS_PERMITIDOS.contains(imagen.getContentType())) {
                throw new IllegalArgumentException("Formato de archivo no permitido.");
            }
            if (imagen.getSize() > TAMANO_MAXIMO) {
                throw new IllegalArgumentException("El archivo excede el tamaño máximo permitido.");
            }

            byte[] bytes = imagen.getBytes();
            return Base64.encodeBase64String(bytes);
        }
        return null;
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
