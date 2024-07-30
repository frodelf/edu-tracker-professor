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
import ua.kpi.edutrackerprofessor.service.ProfessorService;
import ua.kpi.edutrackerprofessor.service.StudentService;

import java.io.IOException;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Value("${sendgrid.api.key}")
    private String apiKey;
    private final StudentService studentService;
    private final ProfessorService professorService;
    private SendGrid sendGrid = new SendGrid(apiKey);

    @Override
    public void sendEmail(EmailDto emailDto) {
//        Email from = new Email("tymur.foshch@avada-media.com");
//        Content content = new Content("text/plain", emailDto.getMessage());
//
//        Request request = new Request();
//        for (String receiver : studentService.getAllEmailsByGroup(emailDto.getGroup())) {
//            Email toEmail = new Email(receiver);
//            Mail mail = new Mail(from, emailDto.getTheme(), toEmail, content);

//            try {
//                request.setMethod(Method.POST);
//                request.setEndpoint("mail/send");
//                request.setBody(mail.build());
//                request.setBaseUri("https://api.sendgrid.net/");
//                Response response = sendGrid.api(request);
//                String responseBody = response.getBody();
//                int statusCode = response.getStatusCode();
//                if (statusCode == 202) {
//                    log.info("Email was sent successfully");
//                    log.info("Response Body: " + responseBody);
//                } else {
//                    log.error("Error - email send");
//                }
//            } catch (IOException ex) {
//                log.error("Error sending email.", ex);
//                throw new RuntimeException("Error sending email.", ex);
//            }
//        }
    }
}