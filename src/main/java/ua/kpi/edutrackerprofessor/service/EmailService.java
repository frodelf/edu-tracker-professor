package ua.kpi.edutrackerprofessor.service;

import ua.kpi.edutrackerprofessor.dto.email.EmailDto;

public interface EmailService {
    void sendEmail(EmailDto emailDto);
}