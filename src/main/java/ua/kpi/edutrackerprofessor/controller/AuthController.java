package ua.kpi.edutrackerprofessor.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackerprofessor.dto.email.EmailDto;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorForRegistrationDto;
import ua.kpi.edutrackerprofessor.service.AuthService;
import ua.kpi.edutrackerprofessor.validation.ContactValidator;
import ua.kpi.edutrackerprofessor.validation.ProfessorValidator;

import java.util.Collections;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final ContactValidator contactValidator;
    private final ProfessorValidator professorValidator;
    @GetMapping("/login")
    public ModelAndView login() {
        if(authService.isAuthenticated()) {return new ModelAndView("redirect:/");}
        return new ModelAndView("auth/login");
    }
    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login";
    }
    @GetMapping("/checkAuth")
    public ResponseEntity<Boolean> checkAuthentication() {
        return ResponseEntity.ok(authService.isAuthenticated());
    }
    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>> registrationForm(@ModelAttribute @Valid ProfessorForRegistrationDto professor, BindingResult bindingResult) throws NoSuchMethodException, MethodArgumentNotValidException {
        contactValidator.validate(professor, bindingResult);
        professorValidator.validate(professor, bindingResult);
        if (bindingResult.hasErrors()) {
            MethodParameter methodParameter = new MethodParameter(this.getClass().getDeclaredMethod("registrationForm", ProfessorForRegistrationDto.class, BindingResult.class), 0);
            throw new MethodArgumentNotValidException(methodParameter, bindingResult);
        }
        return ResponseEntity.ok(Collections.singletonMap("id", authService.registration(professor)));
    }
}