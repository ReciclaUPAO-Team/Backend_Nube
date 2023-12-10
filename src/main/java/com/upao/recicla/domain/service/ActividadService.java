package com.upao.recicla.domain.service;

import com.upao.recicla.domain.entity.Actividad;
import com.upao.recicla.domain.entity.Residuo;
import com.upao.recicla.domain.entity.Usuario;
import com.upao.recicla.infra.email.QrCodeGenerator;
import com.upao.recicla.infra.repository.ActividadRepository;
import com.upao.recicla.infra.repository.ResiduoRepository;
import com.upao.recicla.infra.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActividadService {
    @Autowired
    private final ActividadRepository actividadRepository;

    @Autowired
    private final ResiduoRepository residuoRepository;

    @Autowired
    private final UsuarioRepository usuarioRepository;

    public ActividadService(ActividadRepository actividadRepository,
                            ResiduoRepository residuoRepository,
                            UsuarioRepository usuarioRepository) {
        this.actividadRepository = actividadRepository;
        this.residuoRepository = residuoRepository;
        this.usuarioRepository = usuarioRepository;
    }
    public Page<Actividad> getAllActividades(Pageable pageable) {
        return actividadRepository.findAll(pageable);
    }
    public Optional<Actividad> getActividadById(Long id) {
        return actividadRepository.findById(id);
    }

    public List<Actividad> getActividadesPorUsuarioId(Long usuarioId) {
        return actividadRepository.findByUsuarioId(usuarioId);
    }

    public Actividad getReferenceById(Long id) {
        return actividadRepository.getReferenceById(id);
    }
    public ResponseEntity<String> addActividad(Actividad actividad, String nombreResiduo) {
        if (actividad.getCantidad() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La cantidad no debe ser menor que 0.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(currentUserName)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        actividad.setUsuario(usuario);

        Residuo residuo = residuoRepository.findByNombre(nombreResiduo)
                .orElseThrow(() -> new RuntimeException("Residuo no encontrado"));
        actividad.setResiduo(residuo);

        double puntosGanados = actividad.getCantidad() * residuo.getPuntos();

        actualizarPuntosUsuario(usuario.getId(), puntosGanados);

        actividadRepository.save(actividad);
        return ResponseEntity.ok("Actividad registrada con éxito. Puntos ganados: " + puntosGanados + ".");
    }
    private void actualizarPuntosUsuario(Long idUsuario, double puntos) {
        Usuario usuario = usuarioRepository.findById(idUsuario).get();
        usuario.setPuntos(usuario.getPuntos() + puntos);
        usuario.actualizarNivel();
        usuarioRepository.save(usuario);
    }
    public void updateActividad(Actividad actividad, Long id) {
        Actividad actividadExists = actividadRepository.findById(id)
                .orElse(new Actividad());

        actividadExists.setNombre(actividad.getNombre());
        actividadRepository.save(actividadExists);
    }
    public void deleteActividadById(Long id) {
        actividadRepository.deleteById(id);
    }

    public byte[] generateActividadQRCode(Long actividadId) throws Exception {
        Actividad actividad = getReferenceById(actividadId);
        String qrCodeText = createQRCodeText(actividad);
        return QrCodeGenerator.generateQRCodeImage(qrCodeText, 250, 250);
    }

    private String createQRCodeText(Actividad actividad) {
        // Crear un texto para el código QR
        return String.format(
                "Actividad: %s\nCreador: %s\nCantidad: %.2f\nFecha: %s\nResiduo: %s\nPuntos Usados: %.2f\nPuntos Ganados: %.2f",
                actividad.getNombre(),
                actividad.getUsuario().getNombre(),
                actividad.getCantidad(),
                actividad.getFecha().toString(),
                actividad.getResiduo().getNombre(),
                actividad.getResiduo().getPuntos() * actividad.getCantidad(), // Puntos usados
                actividad.getCantidad() * actividad.getResiduo().getPuntos() // Puntos ganados
        );
    }

}
