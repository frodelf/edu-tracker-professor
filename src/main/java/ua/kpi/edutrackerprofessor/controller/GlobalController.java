package ua.kpi.edutrackerprofessor.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorResponseForGlobal;
import ua.kpi.edutrackerprofessor.service.ProfessorService;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalController {
    private final ProfessorService professorService;

    @ModelAttribute("professor")
    public ProfessorResponseForGlobal globalTeamLeadAttribute() {
        return professorService.getAuthProfessorForGlobal();
    }
}