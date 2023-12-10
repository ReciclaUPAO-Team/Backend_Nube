package com.upao.recicla.infra.email;

import com.upao.recicla.domain.entity.Usuario;
import com.upao.recicla.infra.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailScheduler {
    @Autowired
    private EmailReminderService emailReminderService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PuntosService puntosService;

    // Se ejecuta cada lunes, miércoles y sabado a las 11:00 AM
    @Scheduled(cron = "0 0 11 * * SAT,MON,WED")
    public void enviarRecordatorios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario usuario : usuarios) {
            emailReminderService.enviarRecordatorio(usuario.getCorreo());
        }
    }

    // Se ejecuta cada miércoles y viernes a las 9:00 AM
    @Scheduled(cron = "0 0 9 * * WED,FRI")
    public void enviarAvisosPorPuntos() {
        puntosService.verificarPuntosYEnviarCorreo();
    }
}
