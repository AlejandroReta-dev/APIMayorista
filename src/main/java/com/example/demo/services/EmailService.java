package com.example.demo.services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private final String fromEmail = "Tiendaporconstruccion@gmail.com"; // Cambia esto a tu dirección de correo
    private final String password = "proconstruccion"; // Cambia esto a tu contraseña o una contraseña de aplicación

    // Metodo para enviar el correo electrónico
    public void sendEmail(String toEmail, String subject, String body) {
        // Configurar las propiedades de JavaMail
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com"); // Cambia esto según tu servidor de correo
        properties.put("mail.smtp.port", "587"); // 587 para TLS, 465 para SSL

        // Autenticación de correo
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Crear el mensaje de correo
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            // Enviar el correo
            Transport.send(message);
            System.out.println("Correo enviado exitosamente a " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }
}
