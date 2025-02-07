package ua.kpi.edutrackerprofessor.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorResponseForGlobal;
import ua.kpi.edutrackerprofessor.service.ProfessorService;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalController {
    @Value("${server.host}")
    private String host;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${server.port}")
    private String port;

    private final ProfessorService professorService;

    @ModelAttribute("professor")
    public ProfessorResponseForGlobal globalTeamLeadAttribute() {
        return professorService.getAuthProfessorForGlobal();
    }
    @ModelAttribute("host")
    public String globalHostAttribute() {return host;}
    @ModelAttribute("contextPath")
    public String globalContextPathAttribute() {return contextPath;}
    @ModelAttribute("port")
    public String globalPortAttribute() {return port;}
}