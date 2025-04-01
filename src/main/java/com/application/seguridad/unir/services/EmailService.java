package com.application.seguridad.unir.services;


import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String correoDestino, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(correoDestino);
            helper.setSubject("Verificación OTP");

            String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; background-color: #f9f9f9;'>"
                    + "<h2 style='color: #3f51b5;'>Verificación de Código OTP</h2>"
                    + "<p>Hola,</p>"
                    + "<p>Tu código de verificación es:</p>"
                    + "<div style='font-size: 24px; font-weight: bold; color: #333; margin: 20px 0;'>" + otp + "</div>"
                    + "<p>Este código es válido por los próximos 5 minutos.</p>"
                    + "<p style='font-size: 12px; color: #999;'>Si no solicitaste este código, puedes ignorar este mensaje.</p>"
                    + "</div>";

            helper.setText(htmlContent, true); // ✅ El segundo parámetro true indica HTML

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo OTP", e);
        }
    }
}
