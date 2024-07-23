package ua.kpi.edutrackerprofessor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackerprofessor.dto.email.EmailDto;
import ua.kpi.edutrackerprofessor.service.EmailService;

@Controller
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("email/index");
    }
    @PostMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@ModelAttribute @Valid EmailDto emailDto, BindingResult bindingResult) throws MethodArgumentNotValidException, NoSuchMethodException {
        if(emailDto.getCourse() == null && emailDto.getGroup() == null) bindingResult.rejectValue("group", "", "Має бути вибраний або курс або група");
        if(bindingResult.hasErrors()){
            MethodParameter methodParameter = new MethodParameter(this.getClass().getDeclaredMethod("sendMessage", EmailDto.class, BindingResult.class), 0);
            throw new MethodArgumentNotValidException(methodParameter, bindingResult);
        }
        emailService.sendEmail(emailDto);
        return ResponseEntity.ok("sent");
    }
    @ModelAttribute
    public void activeMenuItem(Model model) {
        model.addAttribute("emailActive", true);
    }
}