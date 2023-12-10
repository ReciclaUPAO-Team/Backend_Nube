package com.upao.recicla.infra.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class EmailReminderService {
    @Autowired
    private JavaMailSender mailSender;

    public void enviarRecordatorio(String email) {
        MimeMessage mensaje = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setTo(email);
            helper.setSubject("¡Tu aporte al reciclaje hace la diferencia!");

            String contenido = "Hola,\n\n" +
                    "Esperamos que estés teniendo un gran día. " +
                    "Queremos recordarte lo importante que es tu participación en las actividades de reciclaje. " +
                    "Cada acción que realizas contribuye a un mundo más limpio y sostenible. " +
                    "¿Listo para hacer tu parte hoy?\n\n" +
                    "Recuerda, cada botella, cada papel, cada pequeño gesto cuenta. " +
                    "Juntos podemos hacer una gran diferencia.\n\n" +
                    "¡Gracias por ser parte de nuestra comunidad eco-amigable!\n\n" +
                    "Saludos cordiales,\n" +
                    "El equipo de Recicla UPAO";
            helper.setText(contenido);

            mailSender.send(mensaje);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

