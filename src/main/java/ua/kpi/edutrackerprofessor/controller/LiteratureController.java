package ua.kpi.edutrackerprofessor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureRequestForAdd;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureRequestForFilter;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureResponseForViewAll;
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
    public ResponseEntity<Page<LiteratureResponseForViewAll>> getAll(@ModelAttribute @Valid LiteratureRequestForFilter literatureRequestForFilter) {
        return ResponseEntity.ok(literatureService.getAll(literatureRequestForFilter));
    }
    @PostMapping("/add")
    public ResponseEntity<Long> add(@ModelAttribute LiteratureRequestForAdd literatureRequestForAdd){
        return ResponseEntity.ok(literatureService.add(literatureRequestForAdd));
    }
    @ModelAttribute
    public void activeMenuItem(Model model) {
        model.addAttribute("literatureActive", true);
    }
}