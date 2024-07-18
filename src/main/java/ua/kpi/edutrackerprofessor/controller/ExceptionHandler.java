package ua.kpi.edutrackerprofessor.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("field", err.getField());
                    error.put("message", err.getDefaultMessage());
                    return error;
                })
                .collect(Collectors.toMap(error -> error.get("field"), error -> error.get("message")));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException ex) {
        return new ModelAndView("error/access_denied", "message", ex.getMessage());
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleAccessDeniedException(EntityNotFoundException ex) {
        return new ModelAndView("error/not_found", "message", ex.getMessage());
    }
}