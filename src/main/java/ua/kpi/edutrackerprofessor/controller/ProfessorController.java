package ua.kpi.edutrackerprofessor.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerprofessor.service.ProfessorService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/professor")
@RequiredArgsConstructor
public class ProfessorController {
    private final ProfessorService professorService;
    @GetMapping("get-first-course")
    public ResponseEntity<Map<String, String>> getFirstCourse() {
        List<Course> courses = professorService.getAuthProfessor().getCourses();
        if(courses.isEmpty()) ResponseEntity.notFound();
        return ResponseEntity.ok(Collections.singletonMap(courses.get(0).getId().toString(), courses.get(0).getName()));
    }
}