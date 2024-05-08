package com.upao.recicla.infra.email;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.upao.recicla.domain.entity.Canje;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.itextpdf.layout.Document;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void enviarCorreoConPDF(String destinatario, Canje canje) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.setMargins(20, 20, 20, 20);

        // Configuración de fuentes y colores
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        // Título del documento
        Paragraph title = new Paragraph("Detalles del Canje")
                .setFont(boldFont)
                .setFontSize(18)
                .setBold()
                .setFontColor(ColorConstants.BLUE)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        // tabla de datos clave
        Table table = new Table(2);
        table.addCell(new Cell().add(new Paragraph("Nro. de Canje reclamado:").setFont(boldFont)));
        table.addCell(new Cell().add(new Paragraph(UUID.randomUUID().toString()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph("Título de la Recompensa:").setFont(boldFont)));
        table.addCell(new Cell().add(new Paragraph(canje.getRecompensa().getTitulo()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph("Puntos Necesarios:").setFont(boldFont)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(canje.getPuntosCanjear())).setFont(font)));
        table.addCell(new Cell().add(new Paragraph("Fecha de Canje:").setFont(boldFont)));
        table.addCell(new Cell().add(new Paragraph(canje.getFecha().toString()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph("Descripción:").setFont(boldFont)));
        table.addCell(new Cell().add(new Paragraph(canje.getRecompensa().getDescripcion()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph("Categoría:").setFont(boldFont)));
        table.addCell(new Cell().add(new Paragraph(canje.getRecompensa().getCategoria()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph("Nombre del Usuario:").setFont(boldFont)));
        table.addCell(new Cell().add(new Paragraph(canje.getUsuario().getNombre()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph("DNI del Usuario:").setFont(boldFont)));
        table.addCell(new Cell().add(new Paragraph(canje.getUsuario().getDni()).setFont(font)));
        document.add(table);

        document.close();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());


        helper.setTo(destinatario);
        helper.setSubject("Detalles de tu Canje en Recicla UPAO");
        String textoCompleto = "¡Felicidades por su recompensa de reciclaje!\n\n" +
                "Estimado(a) Participante.\n\n" +
                "El sector de responsabilidad social de la UPAO lo felicita por haber canjeado una recompensa de reciclaje.\n\n" +
                "El reciclaje es una práctica fundamental para el cuidado del medio ambiente. Al reciclar, ayudamos a reducir la contaminación, conservar los recursos naturales y proteger la salud humana.\n\n" +
                "La UPAO está comprometida con el reciclaje y siempre está motivando a sus estudiantes, docentes y administrativos a participar en esta importante iniciativa.\n\n" +
                "En reconocimiento a su esfuerzo y compromiso con el medio ambiente, Recicla UPAO le otorga una recompensa.\n\n" +
                "Esperamos que disfrute de su recompensa y que continúe reciclando para seguir contribuyendo con el cuidado del medio ambiente.\n\n" +
                "Para reclamar su recompensa, debe acercarse a las oficinas de responsabilidad social en el pabellón D de la UPAO, en horario de 8:00 am. a 5:00 pm.\n\n" +
                "Recuerde que, para reclamar su recompensa, debe presentar su DNI y el código de canje que se encuentra en el archivo adjunto.\n\n" +
                "Atentamente, Sector de responsabilidad social\n" +
                "Universidad Privada Antenor Orrego\n\n" +
                "Este es un mensaje automático. Por favor, no responda a este correo.";
        helper.setText(textoCompleto);
        helper.addAttachment("Canje.pdf", new ByteArrayResource(outputStream.toByteArray()));

        mailSender.send(message);
    }

    public void enviarCorreoPorPuntos(String correo, double puntos) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String contenido = "Has alcanzado " + puntos + " puntos. ¡Ya puedes canjear una recompensa!";
        String asunto = "Puntos acumulados para recompensa";

        try {
            helper.setTo(correo);
            helper.setText(contenido, true);
            helper.setSubject(asunto);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
