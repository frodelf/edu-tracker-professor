package ua.kpi.edutrackerprofessor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureRequestForFilter;
import ua.kpi.edutrackerentity.entity.Literature;
import ua.kpi.edutrackerprofessor.service.LiteratureService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/literature")
@RequiredArgsConstructor
public class LiteratureController {
    private final LiteratureService literatureService;
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("literature/index");
    }
    @GetMapping("/get-all")
    public ResponseEntity<Page<Literature>> getAll(@ModelAttribute @Valid LiteratureRequestForFilter literatureRequestForFilter) {
        return ResponseEntity.ok(literatureService.getAll(literatureRequestForFilter));
    }
    @ModelAttribute
    public void activeMenuItem(Model model) {
        model.addAttribute("literatureActive", true);
    }
}