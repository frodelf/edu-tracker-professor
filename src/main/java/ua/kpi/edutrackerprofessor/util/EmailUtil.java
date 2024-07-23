package ua.kpi.edutrackerprofessor.util;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;

public class EmailUtil {
    @Value("${sendgrid.api.key}")
    private String apiKey;
    private SendGrid sendGrid= new SendGrid(apiKey);

//    public static void sendEmail(EmailDto emailDto) {
//        Request request = new Request();
//        Email from = new Email("tymur.foshch@avada-media.com");
//        Email toEmail = new Email(to);
//
//        Content content = new Content("text/plain", text);
//        Mail mail = new Mail(from, subject, toEmail, content);
//
//        try {
//            request.setMethod(Method.POST);
//            request.setEndpoint("mail/send");
//            request.setBody(mail.build());
//            request.setBaseUri("https://api.sendgrid.net/");
//            Response response = sendGrid.api(request);
//            String responseBody = response.getBody();
//            int statusCode = response.getStatusCode();
//            if (statusCode == 202) {
//                System.out.println("Email was sent successfully");
//                System.out.println("Response Body: " + responseBody);
//            } else {
//                System.out.println("Error - email send");
//            }
//        } catch (IOException ex) {
//            log.error("Error sending email.", ex);
//            throw new RuntimeException("Error sending email.", ex); // or handle it appropriately
//        }
//    }
}