package ua.kpi.edutrackerprofessor.service.impl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackerprofessor.dto.email.EmailDto;
import ua.kpi.edutrackerprofessor.service.EmailService;
import ua.kpi.edutrackerprofessor.service.StudentService;

import java.io.IOException;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Value("${sendgrid.api.key}")
    private String apiKey;
    @Value("${sendgrid.api.from}")
    private String fromEmail;
    private final StudentService studentService;

    @Override
    public void sendEmail(EmailDto emailDto) {
        Email from = new Email(fromEmail);
        Content content = new Content("text/plain", emailDto.getMessage());

        for (String receiver : studentService.getAllEmailsByGroup(emailDto.getGroup())) {

            Email to = new Email(receiver);
            Mail mail = new Mail(from, emailDto.getTheme(), to, content);

            SendGrid sendGrid = new SendGrid(apiKey);
            Request request = new Request();
            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sendGrid.api(request);
                int status = response.getStatusCode();
                System.out.println(status);
            } catch (IOException ex) {
                log.error("Error sending email.", ex);
                throw new RuntimeException("Error sending email.", ex);
            }
        }
    }
}