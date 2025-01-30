package ua.kpi.edutrackerprofessor.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExceptionHandlerTest {

    private final ExceptionHandler exceptionHandler = new ExceptionHandler();

    @Test
    void handleMethodArgumentNotValid() {
        BindingResult bindingResult = mock(BindingResult.class);

        org.springframework.validation.FieldError fieldError = mock(org.springframework.validation.FieldError.class);
        when(fieldError.getField()).thenReturn("fieldName");
        when(fieldError.getDefaultMessage()).thenReturn("Field is invalid");

        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleMethodArgumentNotValid(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Field is invalid", Objects.requireNonNull(response.getBody()).get("fieldName"));
    }

    @Test
    void handleAccessDeniedException() {
        AccessDeniedException ex = new AccessDeniedException("Access Denied");
        ModelAndView modelAndView = exceptionHandler.handleAccessDeniedException(ex);

        assertEquals("error/access_denied", modelAndView.getViewName());
        assertEquals("Access Denied", modelAndView.getModel().get("message"));
    }

    @Test
    void handleEntityNotFoundException() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity Not Found");
        ResponseEntity<String> response = exceptionHandler.handleEntityNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Entity Not Found", response.getBody());
    }
}
