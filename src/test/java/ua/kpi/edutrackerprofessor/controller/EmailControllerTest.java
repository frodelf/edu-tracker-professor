package ua.kpi.edutrackerprofessor.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackerprofessor.dto.email.EmailDto;
import ua.kpi.edutrackerprofessor.service.EmailService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailControllerTest {
    @Mock
    private EmailService emailService;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private Model model;
    @InjectMocks
    private EmailController emailController;

    @Test
    void index() {
        ModelAndView modelAndView = emailController.index();
        assertEquals("email/index", modelAndView.getViewName());
    }

    @Test
    void sendMessageWhenValidInput() throws Exception {
        EmailDto emailDto = new EmailDto();
        emailDto.setCourse(1L);

        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<String> response = emailController.sendMessage(emailDto, bindingResult);

        assertEquals(ResponseEntity.ok("sent"), response);
        verify(emailService, times(1)).sendEmail(emailDto);
    }

    @Test
    void sendMessageWhenNoCourseOrGroup() {
        EmailDto emailDto = new EmailDto();

        when(bindingResult.hasErrors()).thenReturn(true);

        MethodArgumentNotValidException exception = assertThrows(MethodArgumentNotValidException.class,
                () -> emailController.sendMessage(emailDto, bindingResult));

        verify(bindingResult, times(1)).rejectValue("group", "", "Має бути вибраний або курс або група");
        verify(emailService, never()).sendEmail(any());
        assertNotNull(exception);
    }

    @Test
    void sendMessageWhenBindingResultHasErrors() {
        EmailDto emailDto = new EmailDto();
        emailDto.setCourse(1L);

        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(MethodArgumentNotValidException.class,
                () -> emailController.sendMessage(emailDto, bindingResult));

        verify(emailService, never()).sendEmail(any());
    }

    @Test
    void activeMenuItem(){
        emailController.activeMenuItem(model);

        verify(model).addAttribute("emailActive", true);
    }
}