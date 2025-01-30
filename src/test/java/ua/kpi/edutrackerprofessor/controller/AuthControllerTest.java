package ua.kpi.edutrackerprofessor.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorDtoForRegistration;
import ua.kpi.edutrackerprofessor.service.ProfessorService;
import ua.kpi.edutrackerprofessor.validation.ContactValidator;
import ua.kpi.edutrackerprofessor.validation.ProfessorValidator;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private ProfessorService professorService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private ContactValidator contactValidator;
    @Mock
    private ProfessorValidator professorValidator;
    @InjectMocks
    private AuthController authController;

    @Test
    void login() {
        when(professorService.isAuthenticated()).thenReturn(false);
        ModelAndView result = authController.login();
        assertEquals("auth/login", result.getViewName());

        when(professorService.isAuthenticated()).thenReturn(true);
        result = authController.login();
        assertEquals("redirect:/", result.getViewName());
    }

    @Test
    void logoutPage() throws IOException {
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        authController.logoutPage(request, response);

        verify(response, times(0)).sendRedirect(anyString());
    }

    @Test
    void checkAuthentication() {
        when(professorService.isAuthenticated()).thenReturn(true);
        ResponseEntity<Boolean> result = authController.checkAuthentication();
        assertEquals(Boolean.TRUE, result.getBody());

        when(professorService.isAuthenticated()).thenReturn(false);
        result = authController.checkAuthentication();
        assertNotEquals(Boolean.TRUE, result.getBody());
    }

    @Test
    void registrationFormWhenValidInput() throws Exception {
        ProfessorDtoForRegistration professor = new ProfessorDtoForRegistration();
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<ProfessorDtoForRegistration> result = authController.registrationForm(professor, bindingResult);

        assertEquals(ResponseEntity.ok(professor), result);
        verify(professorService, times(1)).registration(professor);
    }

    @Test
    void registrationFormWhenInvalidInput() {
        ProfessorDtoForRegistration professor = new ProfessorDtoForRegistration();
        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(Exception.class, () -> authController.registrationForm(professor, bindingResult));
        verify(professorService, never()).registration(any());
    }
}
