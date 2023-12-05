package Backend.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;

    public void sendVerificationEmail(String to, String verificationLink) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(to);
        helper.setFrom(email);
        helper.setSubject("Bienvenido a FilmBook - Verificación de cuenta");

        String htmlContent = "<html><body style=\"text-align: center;\">"
                + "<div style=\"background-color: #f2f2f2; padding: 20px; border-radius: 10px;\">"
                + "<h2 style=\"color: #333;\">¡Bienvenido a FilmBook!</h2>"
                + "<p style=\"color: #555;\">Gracias por registrarte con FilmBook. Por favor, haz clic en el enlace de abajo para verificar tu cuenta:</p>"
                + "<a href=\"" + verificationLink + "\" style=\"background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;\">Verificar cuenta</a>"
                + "</div>"
                + "</body></html>";

        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

    public void sendReservationConfirmationEmail(String to, String productName, String formattedStartDate, String formattedEndDate, double totalPrice) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(to);
        helper.setFrom(email);
        helper.setSubject("Confirmación de Reserva en FilmBook");

        String htmlContent = "<html><body style=\"text-align: center;\">"
                + "<div style=\"background-color: #f2f2f2; padding: 20px; border-radius: 10px;\">"
                + "<h2 style=\"color: #333;\">¡Confirmación de Reserva!</h2>"
                + "<p style=\"color: #555;\">Has realizado una reserva en FilmBook. A continuación se encuentran los detalles:</p>"
                + "<p style=\"color: #555;\"><strong>Producto:</strong> " + productName + "</p>"
                + "<p style=\"color: #555;\"><strong>Fecha de inicio:</strong> " + formattedStartDate + "</p>"
                + "<p style=\"color: #555;\"><strong>Fecha de fin:</strong> " + formattedEndDate + "</p>"
                + "<p style=\"color: #555;\"><strong>Precio Total:</strong> " + totalPrice + "$" + "</p>"
                + "</div>"
                + "</body></html>";

        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }
}