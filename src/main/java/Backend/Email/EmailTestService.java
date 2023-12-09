package Backend.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailTestService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;

    public void sendTestEmail(String to) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(to);
        helper.setFrom(email);
        helper.setSubject("Test Email");

        String htmlContent = "<html><body style=\"text-align: center;\">"
                + "<div style=\"background-color: #f2f2f2; padding: 20px; border-radius: 10px;\">"
                + "<h2 style=\"color: #333;\">Hello!</h2>"
                + "<p style=\"color: #555;\">This is a test email from your Spring Boot application. Below is a card with buttons:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\">"
                + "<h3 style=\"color: #333;\">Card Content</h3>"
                + "<p style=\"color: #555;\">This is the content of the card.</p>"
                + "<a href=\"#\" style=\"background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; margin-right: 10px;\">Button 1</a>"
                + "<a href=\"#\" style=\"background-color: #008CBA; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;\">Button 2</a>"
                + "</div>"
                + "</div>"
                + "</body></html>";

        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }
}