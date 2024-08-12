package ua.kpi.edutrackerprofessor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorRequestForPersonalData;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorResponseForPersonalData;
import ua.kpi.edutrackerprofessor.service.ProfessorService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/professor")
@RequiredArgsConstructor
public class ProfessorController {
    private final ProfessorService professorService;
    @GetMapping("/get-first-course")
    public ResponseEntity<Map<String, String>> getFirstCourse() {
        List<Course> courses = professorService.getAuthProfessor().getCourses();
        if(courses.isEmpty()) ResponseEntity.notFound();
        return ResponseEntity.ok(Collections.singletonMap(courses.get(0).getId().toString(), courses.get(0).getName()));
    }
    @GetMapping("/personal-data")
    public ModelAndView getPersonalData() {
        return new ModelAndView("professor/personal-data");
    }
    @PutMapping("/update-personal-data")
    public ResponseEntity<String> updatePersonalData(@ModelAttribute @Valid ProfessorRequestForPersonalData personalData) {
        professorService.updatePersonalData(personalData);
        return ResponseEntity.ok("saved");
    }
    @GetMapping("/get-personal-data")
    public ResponseEntity<ProfessorResponseForPersonalData> getAuthProfessor() {
        return ResponseEntity.ok(professorService.getAuthProfessorForPersonalData());
    }
}