package ua.kpi.edutrackerprofessor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.kpi.edutrackerprofessor.validation.annotation.EmailFormat;
import ua.kpi.edutrackerprofessor.validation.annotation.PhoneFormat;
import ua.kpi.edutrackerprofessor.validation.annotation.TelegramFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDataDto {
    private Long id;
    @EmailFormat
    private String email;
    @PhoneFormat
    private String phone;
    @TelegramFormat
    private String telegram;
}