package com.upao.recicla.domain.service;

import com.upao.recicla.domain.entity.Canje;
import com.upao.recicla.domain.entity.Recompensa;
import com.upao.recicla.domain.entity.Usuario;
import com.upao.recicla.infra.email.EmailService;
import com.upao.recicla.infra.repository.CanjeRepository;
import com.upao.recicla.infra.repository.RecompensaRepository;
import com.upao.recicla.infra.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CanjeService {

    @Autowired
    private final CanjeRepository canjeRepository;

    @Autowired
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private final RecompensaRepository recompensaRepository;

    @Autowired
    private EmailService emailService;

    public CanjeService(CanjeRepository canjeRepository, UsuarioRepository usuarioRepository,
                        RecompensaRepository recompensaRepository) {
        this.canjeRepository = canjeRepository;
        this.usuarioRepository = usuarioRepository;
        this.recompensaRepository = recompensaRepository;
    }

    @Transactional
    public void canjearPuntos(String nombreRecompensa) throws Exception {
        String nombreUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(nombreUsuario).orElseThrow();
        Recompensa recompensa = recompensaRepository.findByTitulo(nombreRecompensa).orElseThrow();

        if (usuario.getPuntos() < recompensa.getValor()) {
            throw new Exception("No tienes suficientes puntos para canjear esta recompensa");
        }

        usuario.setPuntos(usuario.getPuntos() - recompensa.getValor());
        usuario.actualizarNivel();
        usuarioRepository.save(usuario);

        Canje canje = new Canje();
        canje.setFecha(LocalDate.now());
        canje.setPuntosCanjear(recompensa.getValor());
        canje.setUsuario(usuario);
        canje.setRecompensa(recompensa);

        canjeRepository.save(canje);

        emailService.enviarCorreoConPDF(usuario.getCorreo(), canje);
    }

}
