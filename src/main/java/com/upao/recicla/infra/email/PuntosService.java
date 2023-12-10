package com.upao.recicla.infra.email;

import com.upao.recicla.domain.entity.Usuario;
import com.upao.recicla.infra.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PuntosService {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void verificarPuntosYEnviarCorreo() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        double umbralPuntos = 400; // Umbral de puntos para canjear una recompensa

        for (Usuario usuario : usuarios) {
            if (usuario.getPuntos() >= umbralPuntos) {
                emailService.enviarCorreoPorPuntos(usuario.getCorreo(), usuario.getPuntos());
            }
        }
    }
}
